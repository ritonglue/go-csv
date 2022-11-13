package io.github.ritonglue.gocsv.convert;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

/**
 * <p>
 * {@link Converter} implementation for date, time with DateTimeFormatter
 * </p>
 */

public class DateTimeFormatConverter<T extends TemporalAccessor> implements Converter<TemporalAccessor> {

	private final DateTimeFormatter formatter;
	private final TemporalQuery<T> query;

	public static <T extends TemporalAccessor> DateTimeFormatConverter<T> of(DateTimeFormatter formatter, TemporalQuery<T> query) {
		return new DateTimeFormatConverter<T>(formatter, query);
	}

	private DateTimeFormatConverter(DateTimeFormatter formatter, TemporalQuery<T> query) {
		this.formatter = formatter;
		this.query = query;
	}

	@Override
	public TemporalAccessor getAsObject(String value) {

		// If the specified value is null or zero-length, return null
		if (value == null) {
			return null;
		}
		value = value.strip();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return formatter.parse(value, query);
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

	@Override
	public String getAsString(TemporalAccessor value) {

		if (value == null) {
			return "";
		}
		try {
			return formatter.format(value);
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

}
