package io.github.ritonglue.gocsv.processor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import io.github.ritonglue.gocsv.annotation.CSVBinding;
import io.github.ritonglue.gocsv.annotation.Convert;

public class DateTimeConverterTest {

	@Test
	public void testLocalDate() throws IOException {
		String csv = "31/12/1974\r\n";
		CSVEngine<P1> engine = CSVEngine.builder(P1.class).mode(Mode.ORDER).build();
		List<P1> list = CSVConverterTest.toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		P1 p = list.get(0);
		P1 a = new P1();
		a.setValue(LocalDate.of(1974, Month.DECEMBER, 31));
		assertEquals(a.getValue(), p.getValue());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals("31/12/1974\r\n", tmp);
	}

	public static class P1 {
		@Convert(pattern="dd/MM/yyyy")
		private LocalDate value;

		public LocalDate getValue() {
			return value;
		}

		public void setValue(LocalDate value) {
			this.value = value;
		}
	}

	@Test
	public void testLocalDateTime() throws IOException {
		String csv = "31/12/1974 23:59\r\n";
		CSVEngine<P2> engine = CSVEngine.builder(P2.class).mode(Mode.ORDER).build();
		List<P2> list = CSVConverterTest.toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		P2 p = list.get(0);
		P2 a = new P2();
		LocalDate date = LocalDate.of(1974, Month.DECEMBER, 31);
		LocalTime time = LocalTime.of(23, 59);
		a.setValue(LocalDateTime.of(date, time));
		assertEquals(a.getValue(), p.getValue());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals("31/12/1974 23:59\r\n", tmp);
	}

	public static class P2 {
		@Convert(pattern="dd/MM/yyyy HH:mm")
		private LocalDateTime value;

		public LocalDateTime getValue() {
			return value;
		}

		public void setValue(LocalDateTime value) {
			this.value = value;
		}
	}

	@Test
	public void testZonedDateTime() throws IOException {
		String csv = "31/12/1974 23:59+01:00\r\n";
		CSVEngine<P3> engine = CSVEngine.builder(P3.class).mode(Mode.ORDER).build();
		List<P3> list = CSVConverterTest.toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		P3 p = list.get(0);
		P3 a = new P3();
		LocalDate date = LocalDate.of(1974, Month.DECEMBER, 31);
		LocalTime time = LocalTime.of(23, 59);
		a.setValue(ZonedDateTime.of(LocalDateTime.of(date, time), ZoneOffset.ofHours(1)));
		assertEquals(a.getValue(), p.getValue());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals("31/12/1974 23:59+01:00\r\n", tmp);
	}

	public static class P3 {
		@Convert(pattern="dd/MM/yyyy HH:mmxxx")
		private ZonedDateTime value;

		public ZonedDateTime getValue() {
			return value;
		}

		public void setValue(ZonedDateTime value) {
			this.value = value;
		}
	}

	@Test
	public void testOffsetDateTime() throws IOException {
		String csv = "31/12/1974 23:59+01:00\r\n";
		CSVEngine<P4> engine = CSVEngine.builder(P4.class).mode(Mode.ORDER).build();
		List<P4> list = CSVConverterTest.toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		P4 p = list.get(0);
		P4 a = new P4();
		LocalDate date = LocalDate.of(1974, Month.DECEMBER, 31);
		LocalTime time = LocalTime.of(23, 59);
		a.setValue(OffsetDateTime.of(LocalDateTime.of(date, time), ZoneOffset.ofHours(1)));
		assertEquals(a.getValue(), p.getValue());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals("31/12/1974 23:59+01:00\r\n", tmp);
	}

	public static class P4 {
		@Convert(pattern="dd/MM/yyyy HH:mmxxx")
		private OffsetDateTime value;

		public OffsetDateTime getValue() {
			return value;
		}

		public void setValue(OffsetDateTime value) {
			this.value = value;
		}
	}

	@Test
	public void testDefaultDateTime() throws IOException {
		String csv = "1974-12-31,1974-12-31T23:59:59,1974-12-31T23:59:59+01:00,1974-12-31T23:59:59+01:00,1974-12-31T23:59:59Z,23:59:59,23:59:59+01:00\r\n";
		CSVEngine<P5> engine = CSVEngine.builder(P5.class).mode(Mode.ORDER).build();
		List<P5> list = CSVConverterTest.toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		P5 p = list.get(0);
		P5 a = new P5();
		LocalDate localDate = LocalDate.of(1974, Month.DECEMBER, 31);
		LocalTime localTime = LocalTime.of(23, 59, 59);
		OffsetTime offsetTime = OffsetTime.of(localTime, ZoneOffset.ofHours(1));
		LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
		OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(1));
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.ofHours(1));
		Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
		a.setLocalDate(localDate);
		a.setZonedDateTime(zonedDateTime);
		a.setOffsetDateTime(offsetDateTime);
		a.setLocalDateTime(localDateTime);
		a.setOffsetTime(offsetTime);
		a.setLocalTime(localTime);
		a.setInstant(instant);
		assertEquals(a.getLocalDate(), p.getLocalDate());
		assertEquals(a.getZonedDateTime(), p.getZonedDateTime());
		assertEquals(a.getOffsetDateTime(), p.getOffsetDateTime());
		assertEquals(a.getLocalDateTime(), p.getLocalDateTime());
		assertEquals(a.getOffsetTime(), p.getOffsetTime());
		assertEquals(a.getLocalTime(), p.getLocalTime());
		assertEquals(a.getInstant(), p.getInstant());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals(csv, tmp);
	}

	public static class P5 {
		@CSVBinding(order = 0)
		private LocalDate localDate;
		@CSVBinding(order = 1)
		private LocalDateTime localDateTime;
		@CSVBinding(order = 2)
		private OffsetDateTime offsetDateTime;
		@CSVBinding(order = 3)
		private ZonedDateTime zonedDateTime;
		@CSVBinding(order = 4)
		private Instant instant;
		@CSVBinding(order = 5)
		private LocalTime localTime;
		@CSVBinding(order = 6)
		private OffsetTime offsetTime;

		public LocalDate getLocalDate() {
			return localDate;
		}
		public void setLocalDate(LocalDate localDate) {
			this.localDate = localDate;
		}
		public LocalDateTime getLocalDateTime() {
			return localDateTime;
		}
		public void setLocalDateTime(LocalDateTime localDateTime) {
			this.localDateTime = localDateTime;
		}
		public OffsetDateTime getOffsetDateTime() {
			return offsetDateTime;
		}
		public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
			this.offsetDateTime = offsetDateTime;
		}
		public Instant getInstant() {
			return instant;
		}
		public ZonedDateTime getZonedDateTime() {
			return zonedDateTime;
		}
		public void setZonedDateTime(ZonedDateTime zonedDateTime) {
			this.zonedDateTime = zonedDateTime;
		}
		public void setInstant(Instant instant) {
			this.instant = instant;
		}
		public LocalTime getLocalTime() {
			return localTime;
		}
		public void setLocalTime(LocalTime localTime) {
			this.localTime = localTime;
		}
		public OffsetTime getOffsetTime() {
			return offsetTime;
		}
		public void setOffsetTime(OffsetTime offsetTime) {
			this.offsetTime = offsetTime;
		}
	}

	@Test
	public void testLocalTimeOffsetTime() throws IOException {
		String csv = "10:10 PM,22:10+01:00\r\n";
		CSVEngine<P6> engine = CSVEngine.builder(P6.class).mode(Mode.ORDER).build();
		List<P6> list = CSVConverterTest.toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		P6 p = list.get(0);
		P6 a = new P6();
		LocalTime time = LocalTime.of(22, 10);
		a.setValue(time);
		a.setValue2(OffsetTime.of(time, ZoneOffset.ofHours(1)));
		assertEquals(a.getValue(), p.getValue());
		assertEquals(a.getValue2(), p.getValue2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals(csv, tmp);
	}

	public static class P6 {
		@CSVBinding(order=0)
		@Convert(pattern="hh:mm a")
		private LocalTime value;
		@CSVBinding(order=1)
		@Convert(pattern="HH:mmxxx")
		private OffsetTime value2;

		public OffsetTime getValue2() {
			return value2;
		}

		public void setValue2(OffsetTime value2) {
			this.value2 = value2;
		}

		public LocalTime getValue() {
			return value;
		}

		public void setValue(LocalTime value) {
			this.value = value;
		}
	}
}
