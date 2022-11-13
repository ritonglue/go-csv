package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * {@link Converter} implementation for <code>java.math.String</code> values.
 * </p>
 */

public class StringConverter implements Converter<String> {
	@Override
	public String getAsObject(String value) {
		return value;
	}

	@Override
	public String getAsString(String value) {

		// If the specified value is null, return a zero-length String
		if (value == null) {
			return "";
		}

		return value;
	}
}
