package io.github.ritonglue.gocsv.convert;

import java.math.BigDecimal;

/**
 * <p>
 * {@link Converter} implementation for <code>java.math.BigDecimal</code> values.
 * </p>
 */

public class BigDecimalConverter implements Converter<BigDecimal> {
	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public BigDecimal getAsObject(String value) {
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return new BigDecimal(value);
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
	public String getAsString(BigDecimal value) {

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
