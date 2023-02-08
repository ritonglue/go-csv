package io.github.ritonglue.gocsv.processor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import io.github.ritonglue.gocsv.annotation.Access;
import io.github.ritonglue.gocsv.annotation.AccessType;
import io.github.ritonglue.gocsv.annotation.CSVBinding;
import io.github.ritonglue.gocsv.annotation.Convert;
import io.github.ritonglue.gocsv.annotation.Transient;
import io.github.ritonglue.gocsv.convert.Converter;
import io.github.ritonglue.gocsv.convert.DateTimeConverter;
import io.github.ritonglue.gocsv.convert.DefaultConverters;
import io.github.ritonglue.gocsv.convert.EnumConverter;

public class CSVEngine<T> {
	private static final String[] EMPTY_STRINGS = new String[0];

	private final Mode mode;
	private final Class<T> clazz;
	private final Predicate<CSVRecord> predicate;
	private final RecordHandler recordHandler;
	private final List<AnnotationStorer> storers = new ArrayList<>();
	private final CallbackIndex callback;
	private final Map<Class<?>, Converter<?>> converters;
	private final Map<CallbackEnum, Consumer<? super T>> callbacks = new EnumMap<>(CallbackEnum.class);

	public static class Builder<T> {
		private Mode mode;
		private Class<T> clazz;
		private Predicate<CSVRecord> predicate;
		private RecordHandler recordHandler;
		private CallbackIndex callback;
		private Map<Class<?>, Converter<?>> converters;

		public Builder<T> mode(Mode mode) {this.mode = mode; return this;}
		public Builder<T> clazz(Class<T> clazz) {this.clazz = clazz; return this;}
		public Builder<T> filter(Predicate<CSVRecord> predicate) {this.predicate = predicate; return this;}
		public Builder<T> callback(CallbackIndex callback) {this.callback = callback; return this;}
		public Builder<T> register(Map<Class<?>, Converter<?>> converters) {this.converters = converters; return this;}
		public <U> Builder<T> register(Class<U> clazz, Class<? extends Converter<U>> converterClazz) {
			try {
				Converter<U> converter = converterClazz.getDeclaredConstructor().newInstance();
				return register(clazz, converter);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}
		public <U> Builder<T> register(Class<U> clazz, Converter<U> converter) {
			if(this.converters == null) this.converters = new HashMap<>();
			this.converters.put(clazz, converter);
			return this;
		}

		public CSVEngine<T> build() {
			return new CSVEngine<>(this);
		}
	}

	public static <T> Builder<T> builder(Class<T> clazz) {
		return new Builder<T>().clazz(clazz);
	}

	private CSVEngine(Builder<T> b) {
		this.mode = Objects.requireNonNull(b.mode, "mode null");
		this.clazz = Objects.requireNonNull(b.clazz, "clazz null");
		this.predicate = b.predicate == null ? o -> true : b.predicate;
		this.recordHandler = b.recordHandler;
		this.callback = b.callback;
		this.converters = new HashMap<>();
		this.converters.putAll(DefaultConverters.getConverters());
		//replace or add new converters
		if(b.converters != null) {
			this.converters.putAll(b.converters);
		}
		init();
	}

	/**
	 * Creates a new instance of the T class object
	 * @return  a newly allocated instance of the class represented by this object.
	 */
	protected T newInstance() {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private void init() {
		try {
			initImpl();
		} catch (ReflectiveOperationException | IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	private static class Data {
		private Converter<?> converter;
		private String header;
		private int order;
	}

	private static boolean isFieldAccess(Class<?> clazz, PropertyDescriptor[] pds) {
		boolean isFieldAccess = true;
		if(clazz.isAnnotationPresent(Access.class)) {
			AccessType accessType = clazz.getAnnotation(Access.class).value();
			if(accessType == null) accessType = AccessType.FIELD;
			switch(accessType) {
			case FIELD:
				isFieldAccess = true;
				break;
			case PROPERTY:
				isFieldAccess = false;
				break;
			}
		} else {
			for(PropertyDescriptor pd : pds) {
				Method getter = pd.getReadMethod();
				if(getter == null) continue;
				if(getter.isAnnotationPresent(CSVBinding.class)) {
					isFieldAccess = false;
					break;
				}
				if(getter.isAnnotationPresent(Convert.class)) {
					isFieldAccess = false;
					break;
				}
			}
		}
		return isFieldAccess;
	}

	private Data getData(AccessibleObject o, String name, Class<?> type) throws ReflectiveOperationException {
		if(o == null) return null;
		int order = 0;
		String header = name;
		String pattern = "";
		Converter<?> converter = null;
		if(o.isAnnotationPresent(Transient.class)) {
			return null;
		}
		if(o.isAnnotationPresent(CSVBinding.class)) {
			CSVBinding binding = o.getAnnotation(CSVBinding.class);
			order = binding.order();
			header = binding.header();
		}
		if(header == null || header.isEmpty()) {
			header = name;
		}
		if(o.isAnnotationPresent(Convert.class)) {
			Convert aConvert = o.getAnnotation(Convert.class);
			Class<?> converterClazz = aConvert.converter();
			try {
				converter = (Converter<?>) converterClazz.getDeclaredConstructor().newInstance();
			} catch(NoSuchMethodException e) {
				//no default constructor
			}
			pattern = aConvert.pattern();
		}
		if(converter == null) {
			if(!pattern.isEmpty()) {
				converter = new DateTimeConverter(pattern, null, type);
			}
		}
		if(converter == null) {
			if(type.isEnum()) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				EnumConverter tmp = new EnumConverter(type);
				this.converters.put(type, tmp);
			}
			//auto-apply
			converter = this.converters.get(type);
		}
		Objects.requireNonNull(converter, "converter null: " + name);
		Data data = new Data();
		data.order = order;
		data.converter = converter;
		data.header = header;
		return data;
	}

	private void initImpl() throws ReflectiveOperationException, IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(clazz, Object.class);
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		boolean isFieldAccess = isFieldAccess(clazz, pds);

		if(isFieldAccess) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields) {
				int modifiers = field.getModifiers();
				if(Modifier.isStatic(modifiers)) continue;
				String name = field.getName();
				Data data = getData(field, name, field.getType());
				if(data != null) {
					AnnotationStorer storer = new AnnotationStorer(field, data.converter, data.order, data.header);
					storers.add(storer);
				}
			}
		} else {
			for(PropertyDescriptor pd : pds) {
				String name = pd.getName();
				Method getter = pd.getReadMethod();
				if(getter == null) continue;
				int modifiers = getter.getModifiers();
				if(Modifier.isStatic(modifiers)) continue;
				Data data = getData(getter, name, getter.getReturnType());
				if(data != null) {
					AnnotationStorer storer = new AnnotationStorer(pd, data.converter, data.order, data.header);
					storers.add(storer);
				}
			}
		}

		//sort : only for writing
		Collections.sort(storers, Comparator.comparingInt(AnnotationStorer::getOrder));

		//check duplicate
		Map<String, AnnotationStorer> mapHeader = new HashMap<>();
		Map<Integer, AnnotationStorer> mapOrder = new HashMap<>();
		for(AnnotationStorer storer : storers) {
			String header = storer.getHeader();
			int order = storer.getOrder();
			Object o = null;
			switch(mode) {
			case NAMED:
				Objects.requireNonNull(header, "header null");
				break;
			case ORDER:
				o = mapOrder.put(order, storer);
				if(o != null) {
					throw new IllegalArgumentException("duplicate order: " + order);
				}
				break;
			}
			if(header != null) {
				o = mapHeader.put(header, storer);
				if(o != null) {
					throw new IllegalArgumentException("duplicate header: " + header);
				}
			}
		}
		readLifeCycle();
	}

	private void readLifeCycle() {
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method : methods) {
			addLifeCycle(CallbackEnum.POST_LOAD, method, "postLoad");
			addLifeCycle(CallbackEnum.POST_PERSIST, method, "postPersist");
			addLifeCycle(CallbackEnum.PRE_PERSIST, method, "prePersist");
		}
	}

	private void addLifeCycle(CallbackEnum callback, Method method, String text) {
		if(callback.hasAnnotation(method)) {
			method.setAccessible(true);
			var o = this.callbacks.put(callback, new Callback(method));
			if(o != null) {
				throw new IllegalStateException("multiple "+ text + " annotation");
			}
		}
	}

	/**
	 * lifeCycle method consumer
	 */
	private static class Callback implements Consumer<Object> {
		private final Method method;

		private Callback(Method method) {
			this.method = method;
		}

		@Override
		public void accept(Object t) {
			try {
				method.invoke(t);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static <E> Stream<E> streamOf(Iterable<E> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}
	@SuppressWarnings("StreamToIterable")
	private static <E> Iterable<E> iterableOf(Stream<E> stream) {
		return stream::iterator;
	}

	public Stream<T> parseAsStream(Reader reader,  CSVFormat format) throws IOException {
		if(mode == Mode.NAMED) {
//			format.withFirstRecordAsHeader();
			format = format.builder().setHeader().setSkipHeaderRecord(true).build();
		}
		CSVParser parser = format.parse(reader);
		Function<AnnotationStorer, Integer> getterIndex = getterIndex(parser);
		return streamOf(parser)
				.filter(this.predicate)
				.filter(Objects::nonNull)
				.map(o -> convert(o, getterIndex));
	}

	public Iterable<T> parse(Reader reader,  CSVFormat format) throws IOException {
		return iterableOf(parseAsStream(reader, format));
	}

	private Function<AnnotationStorer, Integer> getterIndex(CSVParser parser) {
		//index headers
		Function<AnnotationStorer, Integer> getterIndex = null;
		switch(mode) {
		case NAMED:
			Map<String, Integer> headerMap = parser.getHeaderMap();
			getterIndex = o -> {
				String header = o.getHeader();
				return headerMap.get(header);
			};
			break;
		case ORDER:
			getterIndex = AnnotationStorer::getOrder;
			break;
		}
		return getterIndex;
	}

	private T convert(CSVRecord record, Function<AnnotationStorer,Integer> getterIndex) {
		if(record == null) {
			return null;
		}
		T t = newInstance();
		for(AnnotationStorer storer : storers) {
			Integer index = getterIndex.apply(storer);
			if(index == null) {
				missingIndex(storer);
			} else {
				int i = index;
				if(record.isSet(i)) {
					String value = record.get(i);
					setValue(t, value, storer);
				} else {
					outOfRangeIndex(storer);
				}
			}
		}
		Consumer<? super T> postMethod = this.callbacks.get(CallbackEnum.POST_LOAD);
		if(postMethod != null) {
			postMethod.accept(t);
		}
		return t;
	}

	private void missingIndex(AnnotationStorer storer) {
		if(callback != null) callback.missingIndex(storer);
	}

	private void outOfRangeIndex(AnnotationStorer storer) {
		if(callback != null) callback.outOfRangeIndex(storer);
	}

	private void setValue(T t, String value, AnnotationStorer storer) {
		Converter<?> converter = storer.getConverter();
		Object object = converter.getAsObject(value);
		if(recordHandler != null) {
			object = recordHandler.accept(value, object, storer);
		}
		try {
			Field field = storer.getField();
			Method setter = storer.getSetter();
			if(field != null) {
				field.setAccessible(true);
				field.set(t, object);
			} else if(setter != null) {
				setter.setAccessible(true);
				setter.invoke(t, object);
			} else {
				throw new AssertionError("no field, no setter");
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(Iterable<? extends T> list, RecordPrinter printer) throws IOException {
		try {
			Consumer<? super T> postPersist = callbacks.get(CallbackEnum.POST_PERSIST);
			Consumer<? super T> prePersist = callbacks.get(CallbackEnum.PRE_PERSIST);
			for(T t : list) {
				List<String> values = new ArrayList<>();

				if(prePersist != null) {
					prePersist.accept(t);
				}

				for(AnnotationStorer a : storers) {
					Field field = a.getField();
					Method getter = a.getGetter();
					@SuppressWarnings("rawtypes")
					Converter converter = a.getConverter();
					Object value = null;
					if(field != null) {
						field.setAccessible(true);
						value = field.get(t);
					} else if(getter != null) {
						getter.setAccessible(true);
						value = getter.invoke(t);
					}
					@SuppressWarnings("unchecked")
					String s = converter.getAsString(value);
					values.add(s);
				}

				printer.printRecord(values);
				if(postPersist != null) {
					postPersist.accept(t);
				}
			}
		} catch(IOException ee) {
			throw ee;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void write(Iterable<? extends T> list, Appendable appendable, CSVFormat format) throws IOException {
		if(mode == Mode.NAMED) {
			String[] headers = storers.stream().map(AnnotationStorer::getHeader)
				.collect(Collectors.toList()).toArray(EMPTY_STRINGS);
	//		format.withHeader(headers);
			format = format.builder().setHeader(headers).build();
		}
		try(RecordPrinter printer = new RecordPrinterImpl(appendable, format)) {
			write(list, printer);
		}
	}

	public Mode getMode() {
		return mode;
	}
}
