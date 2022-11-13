package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * {@link Converter} implementation for <code>java.lang.Short</code> (and int primitive) values.
 * </p>
 */

public class ShortConverter implements Converter<Short> {

	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public Short getAsObject(String value) {
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return Short.valueOf(value);
		} catch (NumberFormatException nfe) {
			throw new ConverterException(value, nfe);
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public String getAsString(Short value) {
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
