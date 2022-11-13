package io.github.ritonglue.gocsv.processor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import io.github.ritonglue.gocsv.convert.Converter;

public class AnnotationStorer {
	private final Converter<?> converter;
	private final int order;
	private final String header;
	private final Class<?> clazz;
	private final Field field;
	private final Method getter;
	private final Method setter;

	public AnnotationStorer(Field field, Converter<?> converter, int order, String header) {
		this.order = order;
		this.header = header;
		this.clazz = field.getType();
		this.field = field;
		this.converter = converter;
		this.getter = null;
		this.setter = null;
		Objects.requireNonNull(converter, "converter null: " + order);
	}

	public AnnotationStorer(PropertyDescriptor pd, Converter<?> converter, int order, String header) {
		this.order = order;
		this.header = header;
		this.getter = pd.getReadMethod();
		this.setter = pd.getWriteMethod();
		this.clazz = getter.getReturnType();
		this.field = null;
		this.converter = converter;
		Objects.requireNonNull(converter, "converter null: " + order);
	}

	public Field getField() {
		return field;
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}

	public Converter<?> getConverter() {
		return converter;
	}

	public int getOrder() {
		return order;
	}

	public String getHeader() {
		return header;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
