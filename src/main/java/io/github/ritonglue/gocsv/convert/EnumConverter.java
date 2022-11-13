package io.github.ritonglue.gocsv.convert;

import java.util.Objects;

/**
 * <p>
 * {@link Converter} implementation for <code>java.lang.Enum</code> (and enum primitive) values.
 * </p>
 */
public class EnumConverter<T extends Enum<T>> implements Converter<T> {


	private Class<T> targetClass;

	/**
	 * Instantiates an enum converter with a class where enum constants are taken from.
	 *
	 * @param targetClass Class where the enum constants are taken from by the converter methods.
	 */
	public EnumConverter(Class<T> targetClass) {
		this.targetClass = Objects.requireNonNull(targetClass, "targetClass null");
	}

	/**
	 * <p>
	 * Convert the <code>value</code> argument to one of the enum constants of the class provided in our constructor.
	 * </p>
	 *
	 * @param value the String <code>value</code> to be converted to <code>Object</code>.
	 * @throws ConverterException {@inheritDoc}
	 */
	@Override
	public T getAsObject(String value) {
		if (value == null) {
			return null;
		}

		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return Enum.valueOf(targetClass, value);
		} catch (IllegalArgumentException e) {
			throw new ConverterException(value, e);
		}
	}

	/**
	 * <p>
	 * Convert the enum constant given by the <code>value</code> argument into a String.
	 * </p>
	 *
	 * @throws ConverterException {@inheritDoc}
	 */
	@Override
	public String getAsString(T value) {
		if (value == null) {
			return "";
		}
		
		return value.name();
	}
}
