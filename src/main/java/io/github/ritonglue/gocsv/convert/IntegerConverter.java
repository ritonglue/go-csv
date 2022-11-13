package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * {@link Converter} implementation for <code>java.lang.Integer</code> (and int primitive) values.
 * </p>
 */

public class IntegerConverter implements Converter<Integer> {

	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public Integer getAsObject(String value) {

		// If the specified value is null or zero-length, return null
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return Integer.valueOf(value);
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
	public String getAsString(Integer value) {

		// If the specified value is null, return a zero-length String
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
