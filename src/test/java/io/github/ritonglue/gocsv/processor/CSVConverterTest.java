package io.github.ritonglue.gocsv.processor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import io.github.ritonglue.gocsv.annotation.Convert;
import io.github.ritonglue.gocsv.convert.Converter;
import io.github.ritonglue.gocsv.convert.ConverterException;

public class CSVConverterTest {

	@Test
	public void testFieldConverter() throws IOException {
		//problem how to define order headers ?
		String csv = "value\r\n12";
		CSVEngine<P1> engine = CSVEngine.builder(P1.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P1> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P1 p = list.get(0);
		A a = new A();
		a.setValue(12);
		assertEquals(a, p.getValue());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals(csv + "\r\n", tmp);
	}

	public static class P1 {
		@Convert(converter=AConverter.class)
		private A value;

		public A getValue() {
			return value;
		}

		public void setValue(A value) {
			this.value = value;
		}
	}

	static <T> List<T> toList(Iterable<T> iterable) {
		List<T> list = new ArrayList<>();
		for(T t : iterable) list.add(t);
		return list;
	}

	@Test
	public void testMethodConverter() throws IOException {
		//problem how to define order headers ?
		String csv = "value\r\n12";
		CSVEngine<P2> engine = CSVEngine.builder(P2.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P2> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P2 p = list.get(0);
		A a = new A();
		a.setValue(12);
		assertEquals(a, p.getValue());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals(csv + "\r\n", tmp);
	}

	public static class P2 {
		private A value;

		@Convert(converter=AConverter.class)
		public A getValue() {
			return value;
		}

		public void setValue(A value) {
			this.value = value;
		}
	}

	public static class A {
		private Integer value;

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value == null ? null : value.toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == this) return true;
			if(!(obj instanceof A)) return false;
			A a = (A) obj;
			return Objects.deepEquals(this.value, a.value);
		}
		
		@Override
		public int hashCode() {
			return value == null ? 0 : value.hashCode();
		}
	}
	
	public static class AConverter implements Converter<A> {
		@Override
		public A getAsObject(String value) {
			if (value == null) {
				return null;
			}
			value = value.strip();
			if (value.isEmpty()) {
				return null;
			}

			try {
				Integer i = Integer.valueOf(value);
				A a = new A();
				a.setValue(i);
				return a;
			} catch (NumberFormatException nfe) {
				throw new ConverterException(value, nfe);
			} catch (Exception e) {
				throw new ConverterException(e);
			}
		}

		@Override
		public String getAsString(A value) {
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
}
