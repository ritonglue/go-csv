package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * {@link Converter} implementation for <code>java.lang.Object</code> values.
 * </p>
 */

public class ObjectConverter implements Converter<Object> {
	/**
	 * @throws ConverterException {@inheritDoc}
	 */
	@Override
	public Object getAsObject(String value) {
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}
		throw new ConverterException("can't convert string to object: " + value);
	}

	/**
	 * @throws ConverterException {@inheritDoc}
	 */
	@Override
	public String getAsString(Object value) {
		if (value == null) {
			return "";
		}
		try {
			return value.toString();
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}
}
