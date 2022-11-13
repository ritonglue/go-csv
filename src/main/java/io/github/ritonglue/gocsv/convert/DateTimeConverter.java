package io.github.ritonglue.gocsv.convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * {@link Converter} implementation for date, time
 * </p>
 */

public class DateTimeConverter implements Converter<TemporalAccessor> {
	private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");
	private String pattern;
	private Locale locale;
	private TimeZone timeZone = DEFAULT_TIME_ZONE;
	private DateTimeFormatter formatter;
	private final Class<?> clazz;

	public DateTimeConverter(String pattern, TimeZone timeZone, Class<?> clazz) {
		this(pattern, null, timeZone, clazz);

	}
	public DateTimeConverter(String pattern, Locale locale, TimeZone timeZone, Class<?> clazz) {
		this.pattern = pattern;
		this.locale = locale;
		this.timeZone = timeZone;
		this.clazz = clazz;
		if(locale == null) {
			this.formatter = DateTimeFormatter.ofPattern(pattern);
		} else {
			this.formatter = DateTimeFormatter.ofPattern(pattern, locale);
		}
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
			if(clazz == LocalDate.class) {
				return LocalDate.parse(value, formatter);
			} else if(clazz == LocalDateTime.class) {
				return LocalDateTime.parse(value, formatter);
			} else if(clazz == ZonedDateTime.class) {
				return ZonedDateTime.parse(value, formatter);
			} else if(clazz == OffsetDateTime.class) {
				return OffsetDateTime.parse(value, formatter);
			} else if(clazz == LocalTime.class) {
				return LocalTime.parse(value, formatter);
			} else if(clazz == OffsetTime.class) {
				return OffsetTime.parse(value, formatter);
			} else {
				throw new IllegalArgumentException("bad clazz: " + clazz);
			}
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

    public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
