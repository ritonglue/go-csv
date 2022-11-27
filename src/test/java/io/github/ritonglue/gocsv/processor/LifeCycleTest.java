package io.github.ritonglue.gocsv.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import io.github.ritonglue.gocsv.annotation.PostLoad;
import io.github.ritonglue.gocsv.annotation.PostPersist;
import io.github.ritonglue.gocsv.annotation.PrePersist;
import io.github.ritonglue.gocsv.annotation.Transient;

public class LifeCycleTest {

	@Test
	public void testPostLoad() throws IOException {
		String csv = "firstname,lastname\r\nJohn,Lewis\r\n";
		CSVEngine<P1> engine = CSVEngine.builder(P1.class)
				.mode(Mode.NAMED)
				.build();
		List<P1> list = engine.parseAsStream(new StringReader(csv), CSVFormat.DEFAULT)
			.collect(Collectors.toList());
		assertEquals(1, list.size());
		P1 p = list.get(0);
		Person person = p.getPerson();
		assertTrue("Person is null", person != null);
		assertEquals("John", person.getFirstname());
		assertEquals("Lewis", person.getLastname());
	}

	public static class Person {
		private final String firstname;
		private final String lastname;

		Person(String firstname, String lastname) {
			this.firstname = firstname;
			this.lastname = lastname;
		}
		public String getFirstname() {
			return firstname;
		}
		public String getLastname() {
			return lastname;
		}
	}

	public static class P1 {
		private String firstname;
		private String lastname;

		@Transient
		private Person person;


		@PostLoad
		private void postLoad() {
			person = new Person(firstname, lastname);
		}

		public Person getPerson() {
			return person;
		}


		public void setPerson(Person person) {
			this.person = person;
		}
	}

	@Test
	public void testPersist() throws IOException {
		String csv = "firstname\r\nJohn\r\n";
		CSVEngine<P2> engine = CSVEngine.builder(P2.class)
				.mode(Mode.NAMED)
				.build();
		List<P2> list = engine.parseAsStream(new StringReader(csv), CSVFormat.DEFAULT)
			.collect(Collectors.toList());
		assertEquals(1, list.size());
		P2 p = list.get(0);
		assertEquals("John", p.getFirstname());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals("firstname\r\njohn\r\n", tmp);
		assertEquals("JOHN", p.getFirstname());
	}

	public static class P2 {
		private String firstname;

		@PrePersist
		private void prePersist() {
			firstname = firstname.toLowerCase();
		}

		@PostPersist
		private void postPersist() {
			firstname = firstname.toUpperCase();
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
	}

	@Test
	public void testJakartaPersistence() throws IOException {
		String csv = "firstname\r\nJohn\r\n";
		CSVEngine<P3> engine = CSVEngine.builder(P3.class)
				.mode(Mode.NAMED)
				.build();
		List<P3> list = engine.parseAsStream(new StringReader(csv), CSVFormat.DEFAULT)
			.collect(Collectors.toList());
		assertEquals(1, list.size());
		P3 p = list.get(0);
		assertEquals("John_", p.getFirstname());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals("firstname\r\njohn_\r\n", tmp);
		assertEquals("JOHN_", p.getFirstname());
	}

	public static class P3 {
		private String firstname;

		@jakarta.persistence.PostLoad
		private void postLoad() {
			firstname = firstname + "_";
		}

		@jakarta.persistence.PrePersist
		private void prePersist() {
			firstname = firstname.toLowerCase();
		}

		@jakarta.persistence.PostPersist
		private void postPersist() {
			firstname = firstname.toUpperCase();
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
	}

	@Test
	public void testJavaxPersistence() throws IOException {
		String csv = "firstname\r\nJohn\r\n";
		CSVEngine<P4> engine = CSVEngine.builder(P4.class)
				.mode(Mode.NAMED)
				.build();
		List<P4> list = engine.parseAsStream(new StringReader(csv), CSVFormat.DEFAULT)
			.collect(Collectors.toList());
		assertEquals(1, list.size());
		P4 p = list.get(0);
		assertEquals("John_", p.getFirstname());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		assertEquals("firstname\r\njohn_\r\n", tmp);
		assertEquals("JOHN_", p.getFirstname());
	}

	public static class P4 {
		private String firstname;

		@javax.persistence.PostLoad
		private void postLoad() {
			firstname = firstname + "_";
		}

		@javax.persistence.PrePersist
		private void prePersist() {
			firstname = firstname.toLowerCase();
		}

		@javax.persistence.PostPersist
		private void postPersist() {
			firstname = firstname.toUpperCase();
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
	}
}
