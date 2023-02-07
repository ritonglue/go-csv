package io.github.ritonglue.gocsv.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class DefaultConverters {
	private final static Map<Class<?>, Converter<?>> MAP;

	static {
		Map<Class<?>, Converter<?>> map = new HashMap<>();
		map.put(byte.class, new ByteConverter());
		map.put(Byte.class, new ByteConverter());
		map.put(boolean.class, new BooleanConverter());
		map.put(Boolean.class, new BooleanConverter());
		map.put(char.class     , new CharacterConverter());
		map.put(Character.class, new CharacterConverter());
		map.put(short.class, new ShortConverter());
		map.put(Short.class, new ShortConverter());
		map.put(int.class, new IntegerConverter());
		map.put(Integer.class, new IntegerConverter());
		map.put(long.class, new LongConverter());
		map.put(Long.class, new LongConverter());
		map.put(double.class, new DoubleConverter());
		map.put(Double.class, new DoubleConverter());
		map.put(float.class, new FloatConverter());
		map.put(Float.class, new FloatConverter());
		map.put(BigDecimal.class, new BigDecimalConverter());
		map.put(BigInteger.class, new BigIntegerConverter());
		map.put(Object.class, new ObjectConverter());
		map.put(String.class, new StringConverter());
		map.put(Currency.class, new CurrencyConverter());

		map.put(LocalDate.class, DateTimeFormatConverter.of(DateTimeFormatter.ISO_LOCAL_DATE, LocalDate::from));
		map.put(LocalDateTime.class, DateTimeFormatConverter.of(DateTimeFormatter.ISO_LOCAL_DATE_TIME, LocalDateTime::from));
		map.put(OffsetDateTime.class, DateTimeFormatConverter.of(DateTimeFormatter.ISO_OFFSET_DATE_TIME, OffsetDateTime::from));
		map.put(ZonedDateTime.class, DateTimeFormatConverter.of(DateTimeFormatter.ISO_ZONED_DATE_TIME, ZonedDateTime::from));
		map.put(Instant.class, DateTimeFormatConverter.of(DateTimeFormatter.ISO_INSTANT, Instant::from));
		map.put(LocalTime.class, DateTimeFormatConverter.of(DateTimeFormatter.ISO_LOCAL_TIME, LocalTime::from));
		map.put(OffsetTime.class, DateTimeFormatConverter.of(DateTimeFormatter.ISO_OFFSET_TIME, OffsetTime::from));

		MAP = Collections.unmodifiableMap(map);
	}
	
	public static Map<Class<?>, Converter<?>> getConverters() {
		return MAP;
	}
}
