package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * {@link Converter} implementation for <code>java.lang.Float</code> (and int primitive) values.
 * </p>
 */

public class FloatConverter implements Converter<Float> {

	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public Float getAsObject(String value) {
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return Float.valueOf(value);
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
	public String getAsString(Float value) {

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
