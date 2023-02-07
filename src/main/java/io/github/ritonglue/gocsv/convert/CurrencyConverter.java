package io.github.ritonglue.gocsv.convert;

import java.util.Currency;

/**
 * <p>
 * {@link Converter} implementation for <code>java.util.Currency</code> values.
 * </p>
 */

public class CurrencyConverter implements Converter<Currency> {
	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public Currency getAsObject(String value) {
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return Currency.getInstance(value);
		} catch (Exception e) {
			throw new ConverterException(e);
		}

	}

	/**
	 * @throws ConverterException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public String getAsString(Currency value) {

		// If the specified value is null, return a zero-length String
		return value == null ? "" : value.toString();
	}
}
