package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * {@link Converter} implementation for <code>java.lang.Byte</code> (and byte primitive) values.
 * </p>
 */

public class ByteConverter implements Converter<Byte> {

	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public Byte getAsObject(String value) {
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return Byte.valueOf(value);
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
	public String getAsString(Byte value) {
		if (value == null) {
			return "";
		}

		try {
            return Byte.toString(value.byteValue());
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}
}
