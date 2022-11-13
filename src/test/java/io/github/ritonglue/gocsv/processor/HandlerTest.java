package io.github.ritonglue.gocsv.processor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import io.github.ritonglue.gocsv.annotation.CSVBinding;

public class HandlerTest {

	@Test
	public void testHandler() throws IOException {
		String csv = "aa\r\nAA\r\nbb";
		CSVEngine<P1> engine = CSVEngine.builder(P1.class)
				.mode(Mode.ORDER)
				.filter(o -> {
					String s = o.get(0);
					return !s.equals(s.toUpperCase());
				})
				.build();
		List<P1> list = engine.parseAsStream(new StringReader(csv), CSVFormat.DEFAULT)
			.collect(Collectors.toList());
		assertEquals(2, list.size());
		P1 p = list.get(0);
		assertEquals("aa", p.getValue());
		p = list.get(1);
		assertEquals("bb", p.getValue());
	}

	public static class P1 {
		@CSVBinding(order=0)
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
