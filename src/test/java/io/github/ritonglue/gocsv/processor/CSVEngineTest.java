package io.github.ritonglue.gocsv.processor;

import static io.github.ritonglue.gocsv.processor.CSVConverterTest.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import io.github.ritonglue.gocsv.annotation.Access;
import io.github.ritonglue.gocsv.annotation.AccessType;
import io.github.ritonglue.gocsv.annotation.CSVBinding;
import io.github.ritonglue.gocsv.annotation.Convert;
import io.github.ritonglue.gocsv.annotation.Transient;
import io.github.ritonglue.gocsv.convert.Converter;
import io.github.ritonglue.gocsv.convert.ConverterException;

public class CSVEngineTest {
	@Test
	public void testPrimitive() throws IOException {
		String csv = "true, true, "//boolean
			+",1,-1, "//short
			+",2,-2,"//int
			+",3,-3, "//long
			+",c,C,,127,-127,,1.1,-1.1,,2.2,-2.2,,99.9,,123,";
		CSVEngine<Pojo> engine = CSVEngine.builder(Pojo.class).mode(Mode.ORDER).build();
		List<Pojo> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		Pojo p = list.get(0);
		assertTrue(p.isBooleanP());
		assertEquals(Boolean.TRUE, p.getIsBooleanB());
		assertNull(p.getIsBooleanN());

		assertEquals(1, p.getShortP());
		assertEquals(Short.valueOf((short)-1), p.getShortB());
		assertNull(p.getShortN());

		assertEquals(2, p.getIntP());
		assertEquals(Integer.valueOf(-2), p.getIntB());
		assertNull(p.getIntN());

		assertEquals(3L, p.getLongP());
		assertEquals(Long.valueOf(-3L), p.getLongB());
		assertNull(p.getLongN());

		assertEquals('c', p.getCharP());
		assertEquals(Character.valueOf('C'), p.getCharB());
		assertNull(p.getCharN());

		assertEquals((byte)127, p.getByteP());
		assertEquals(Byte.valueOf((byte)-127), p.getByteB());
		assertNull(p.getByteN());

		assertEquals(1.1, p.getDoubleP(), 1.e-6);
		assertEquals(Double.valueOf(-1.1), p.getDoubleB(), 1.e-6);
		assertNull(p.getDoubleN());

		assertEquals(2.2f, p.getFloatP(), 1.e-6);
		assertEquals(Float.valueOf(-2.2f), p.getFloatB(), 1.e-6);
		assertNull(p.getFloatN());

		assertEquals(new BigDecimal("99.9"), p.getBigDecimal());
		assertNull(p.getBigDecimalN());

		assertEquals(new BigInteger("123"), p.getBigInteger());
		assertNull(p.getBigIntegerN());

		csv = csv.replaceAll("\\s", "");
		StringWriter writer = new StringWriter();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		engine.write(list, writer, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
		String tmp = writer.toString();
		assertEquals(csv+"\r\n", tmp);
	}

	public static class Pojo {
		@CSVBinding(order = 0)
		private boolean isBooleanP;
		@CSVBinding(order = 1)
		private Boolean isBooleanB;
		@CSVBinding(order = 2)
		private Boolean isBooleanN;
		@CSVBinding(order = 3)
		private short shortP;
		@CSVBinding(order = 4)
		private Short shortB;
		@CSVBinding(order = 5)
		private Short shortN;
		@CSVBinding(order = 6)
		private int intP;
		@CSVBinding(order = 7)
		private Integer intB;
		@CSVBinding(order = 8)
		private Integer intN;
		@CSVBinding(order = 9)
		private long longP;
		@CSVBinding(order = 10)
		private Long longB;
		@CSVBinding(order = 11)
		private Long longN;
		@CSVBinding(order = 12)
		private char charP;
		@CSVBinding(order = 13)
		private Character charB;
		@CSVBinding(order = 14)
		private Character charN;
		@CSVBinding(order = 15)
		private byte byteP;
		@CSVBinding(order = 16)
		private Byte byteB;
		@CSVBinding(order = 17)
		private Byte byteN;
		@CSVBinding(order = 18)
		private double doubleP;
		@CSVBinding(order = 19)
		private Double doubleB;
		@CSVBinding(order = 20)
		private Double doubleN;
		@CSVBinding(order = 21)
		private float floatP;
		@CSVBinding(order = 22)
		private Float floatB;
		@CSVBinding(order = 23)
		private Float floatN;
		@CSVBinding(order = 24)
		private BigDecimal bigDecimal;
		@CSVBinding(order = 25)
		private BigDecimal bigDecimalN;
		@CSVBinding(order = 26)
		private BigInteger bigInteger;
		@CSVBinding(order = 27)
		private BigInteger bigIntegerN;

		public BigDecimal getBigDecimalN() {
			return bigDecimalN;
		}
		public void setBigDecimalN(BigDecimal bigDecimalN) {
			this.bigDecimalN = bigDecimalN;
		}
		public BigInteger getBigIntegerN() {
			return bigIntegerN;
		}
		public void setBigIntegerN(BigInteger bigIntegerN) {
			this.bigIntegerN = bigIntegerN;
		}

		public boolean isBooleanP() {
			return isBooleanP;
		}
		public void setBooleanP(boolean isBooleanP) {
			this.isBooleanP = isBooleanP;
		}
		public Boolean getIsBooleanB() {
			return isBooleanB;
		}
		public void setIsBooleanB(Boolean isBooleanB) {
			this.isBooleanB = isBooleanB;
		}
		public Boolean getIsBooleanN() {
			return isBooleanN;
		}
		public void setIsBooleanN(Boolean isBooleanN) {
			this.isBooleanN = isBooleanN;
		}
		public Short getShortN() {
			return shortN;
		}
		public void setShortN(Short shortN) {
			this.shortN = shortN;
		}
		public Integer getIntN() {
			return intN;
		}
		public void setIntN(Integer intN) {
			this.intN = intN;
		}
		public Long getLongN() {
			return longN;
		}
		public void setLongN(Long longN) {
			this.longN = longN;
		}
		public Character getCharN() {
			return charN;
		}
		public void setCharN(Character charN) {
			this.charN = charN;
		}
		public Byte getByteN() {
			return byteN;
		}
		public void setByteN(Byte byteN) {
			this.byteN = byteN;
		}
		public Double getDoubleN() {
			return doubleN;
		}
		public void setDoubleN(Double doubleN) {
			this.doubleN = doubleN;
		}
		public Float getFloatN() {
			return floatN;
		}
		public void setFloatN(Float floatN) {
			this.floatN = floatN;
		}
		public int getIntP() {
			return intP;
		}
		public void setIntP(int intP) {
			this.intP = intP;
		}
		public Integer getIntB() {
			return intB;
		}
		public void setIntB(Integer intB) {
			this.intB = intB;
		}
		public short getShortP() {
			return shortP;
		}
		public void setShortP(short shortP) {
			this.shortP = shortP;
		}
		public Short getShortB() {
			return shortB;
		}
		public void setShortB(Short shortB) {
			this.shortB = shortB;
		}
		public long getLongP() {
			return longP;
		}
		public void setLongP(long longP) {
			this.longP = longP;
		}
		public Long getLongB() {
			return longB;
		}
		public void setLongB(Long longB) {
			this.longB = longB;
		}
		public char getCharP() {
			return charP;
		}
		public void setCharP(char charP) {
			this.charP = charP;
		}
		public Character getCharB() {
			return charB;
		}
		public void setCharB(Character charB) {
			this.charB = charB;
		}
		public byte getByteP() {
			return byteP;
		}
		public void setByteP(byte byteP) {
			this.byteP = byteP;
		}
		public Byte getByteB() {
			return byteB;
		}
		public void setByteB(Byte byteB) {
			this.byteB = byteB;
		}
		public double getDoubleP() {
			return doubleP;
		}
		public void setDoubleP(double doubleP) {
			this.doubleP = doubleP;
		}
		public Double getDoubleB() {
			return doubleB;
		}
		public void setDoubleB(Double doubleB) {
			this.doubleB = doubleB;
		}
		public float getFloatP() {
			return floatP;
		}
		public void setFloatP(float floatP) {
			this.floatP = floatP;
		}
		public Float getFloatB() {
			return floatB;
		}
		public void setFloatB(Float floatB) {
			this.floatB = floatB;
		}
		public BigDecimal getBigDecimal() {
			return bigDecimal;
		}
		public void setBigDecimal(BigDecimal bigDecimal) {
			this.bigDecimal = bigDecimal;
		}
		public BigInteger getBigInteger() {
			return bigInteger;
		}
		public void setBigInteger(BigInteger bigInteger) {
			this.bigInteger = bigInteger;
		}
	}

	@Test
	public void testFieldNamed() throws IOException {
		//problem how to define order headers ?
		String csv = "value1,value2\r\n1,2";
		CSVEngine<P1> engine = CSVEngine.builder(P1.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P1> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));;
		assertEquals(1, list.size());
		P1 p = list.get(0);
		assertEquals(1,p.getValue1());
		assertEquals(Integer.valueOf(2),p.getValue2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "value1,value2\r\n1,2\r\n";
		assertEquals(csvExpected, tmp);
	}

	public static class P1 {
		private int value1;
		private Integer value2;

		public int getValue1() {
			return value1;
		}
		public void setValue1(int value1) {
			this.value1 = value1;
		}
		public Integer getValue2() {
			return value2;
		}
		public void setValue2(Integer value2) {
			this.value2 = value2;
		}
	}

	@Test
	public void testFieldReNamed() throws IOException {
		//problem how to define order headers ?
		String csv = "val1,val2\r\n1,2";
		CSVEngine<P3> engine = CSVEngine.builder(P3.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P3> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P3 p = list.get(0);
		assertEquals(1,p.getValue1());
		assertEquals(Integer.valueOf(2),p.getValue2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "val1,val2\r\n1,2\r\n";
		assertEquals(csvExpected, tmp);
	}

	public static class P3 {
		@CSVBinding(header="val1")
		private int value1;
		@CSVBinding(header="val2")
		private Integer value2;

		public int getValue1() {
			return value1;
		}
		public void setValue1(int value1) {
			this.value1 = value1;
		}
		public Integer getValue2() {
			return value2;
		}
		public void setValue2(Integer value2) {
			this.value2 = value2;
		}
	}
		
	@Test
	public void testMethodNamed() throws IOException {
		//problem how to define order headers ?
		String csv = "value1,value2\r\n1,2";
		CSVEngine<P2> engine = CSVEngine.builder(P2.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P2> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P2 p = list.get(0);
		assertEquals(1,p.getValue1());
		assertEquals(Integer.valueOf(2),p.getValue2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "value1,value2\r\n1,2\r\n";
		assertEquals(csvExpected, tmp);
	}

	public static class P2 {
		private int value1;
		private Integer value2;

		@CSVBinding
		public int getValue1() {
			return value1;
		}
		public void setValue1(int value1) {
			this.value1 = value1;
		}
		@CSVBinding
		public Integer getValue2() {
			return value2;
		}
		public void setValue2(Integer value2) {
			this.value2 = value2;
		}
	}
		
	@Test
	public void testMethodReNamed() throws IOException {
		//problem how to define order headers ?
		String csv = "val1,val2\r\n1,2";
		CSVEngine<P4> engine = CSVEngine.builder(P4.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P4> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P4 p = list.get(0);
		assertEquals(1,p.getValue1());
		assertEquals(Integer.valueOf(2),p.getValue2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "val1,val2\r\n1,2\r\n";
		assertEquals(csvExpected, tmp);
	}

	public static class P4 {
		private int value1;
		private Integer value2;

		@CSVBinding(header="val1")
		public int getValue1() {
			return value1;
		}
		public void setValue1(int value1) {
			this.value1 = value1;
		}
		@CSVBinding(header="val2")
		public Integer getValue2() {
			return value2;
		}
		public void setValue2(Integer value2) {
			this.value2 = value2;
		}
	}

	@Test
	public void testFieldMultiLineNamed() throws IOException {
		//problem how to define order headers ?
		String csv = "val1,val2\r\n1,2\r\n3,4";
		CSVEngine<P3> engine = CSVEngine.builder(P3.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P3> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(2, list.size());
		P3 p = list.get(0);
		assertEquals(1,p.getValue1());
		assertEquals(Integer.valueOf(2),p.getValue2());

		p = list.get(1);
		assertEquals(3,p.getValue1());
		assertEquals(Integer.valueOf(4),p.getValue2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "val1,val2\r\n1,2\r\n3,4\r\n";
		assertEquals(csvExpected, tmp);
	}

	@Test
	public void testAccessField() throws IOException {
		String csv = "value1,value2\r\n1,2";
		CSVEngine<P5> engine = CSVEngine.builder(P5.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P5> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P5 p = list.get(0);
		assertEquals(1,p.getVal1());
		assertEquals(Integer.valueOf(2),p.getVal2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "value1,value2\r\n1,2\r\n";
		assertEquals(csvExpected, tmp);
	}

	@Access(AccessType.FIELD)
	public static class P5 {
		private int value1;
		private Integer value2;

		public int getVal1() {
			return value1;
		}
		public void setVal1(int value1) {
			this.value1 = value1;
		}
		public Integer getVal2() {
			return value2;
		}
		public void setVal2(Integer value2) {
			this.value2 = value2;
		}
	}

	@Test
	public void testAccessProperty() throws IOException {
		String csv = "val1,val2\r\n1,2";
		CSVEngine<P6> engine = CSVEngine.builder(P6.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P6> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P6 p = list.get(0);
		assertEquals(1,p.getVal1());
		assertEquals(Integer.valueOf(2),p.getVal2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "val1,val2\r\n1,2\r\n";
		assertEquals(csvExpected, tmp);
	}

	@Access(AccessType.PROPERTY)
	public static class P6 {
		private int value1;
		private Integer value2;

		public int getVal1() {
			return value1;
		}
		public void setVal1(int value1) {
			this.value1 = value1;
		}
		public Integer getVal2() {
			return value2;
		}
		public void setVal2(Integer value2) {
			this.value2 = value2;
		}
	}

	@Test
	public void testTransient() throws IOException {
		String csv = "value1,value3\r\n1,bla";
		CSVEngine<P7> engine = CSVEngine.builder(P7.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P7> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P7 p = list.get(0);
		assertEquals(1,p.getVal1());
		assertEquals("bla", p.getVal3());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "value1,value3\r\n1,bla\r\n";
		assertEquals(csvExpected, tmp);
	}

	public static class P7 {
		private int value1;
		@Transient
		private Integer value2;
		private String value3;

		public int getVal1() {
			return value1;
		}
		public void setVal1(int value1) {
			this.value1 = value1;
		}
		public Integer getVal2() {
			return value2;
		}
		public void setVal2(Integer value2) {
			this.value2 = value2;
		}
		public String getVal3() {
			return value3;
		}
		public void setVal3(String value3) {
			this.value3 = value3;
		}
	}

	@Test
	public void testTransientMethod() throws IOException {
		String csv = "val1,val3\r\n1,bla";
		CSVEngine<P8> engine = CSVEngine.builder(P8.class).mode(Mode.NAMED).build();
//		CSVFormat.DEFAULT.withFirstRecordAsHeader();
		List<P8> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()));
		assertEquals(1, list.size());
		P8 p = list.get(0);
		assertEquals(1,p.getVal1());
		assertEquals("bla", p.getVal3());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "val1,val3\r\n1,bla\r\n";
		assertEquals(csvExpected, tmp);
	}

	@Access(AccessType.PROPERTY)
	public static class P8 {
		private int value1;
		private Integer value2;
		private String value3;

		public int getVal1() {
			return value1;
		}
		public void setVal1(int value1) {
			this.value1 = value1;
		}
		@Transient
		public Integer getVal2() {
			return value2;
		}
		public void setVal2(Integer value2) {
			this.value2 = value2;
		}
		public String getVal3() {
			return value3;
		}
		public void setVal3(String value3) {
			this.value3 = value3;
		}
	}

	@Test
	public void testEnum() throws IOException {
		String csv = "BB\r\n";
		CSVEngine<P9> engine = CSVEngine.builder(P9.class).mode(Mode.ORDER).build();
		List<P9> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		P9 p = list.get(0);
		assertEquals(AP.BB, p.getVal1());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = "BB\r\n";
		assertEquals(csvExpected, tmp);
	}

	enum AP {AA,BB};
	public static class P9 {
		@CSVBinding(order = 0)
		private AP val1;

		public AP getVal1() {
			return val1;
		}

		public void setVal1(AP val1) {
			this.val1 = val1;
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDuplicateOrder() {
		CSVEngine.builder(A1.class).mode(Mode.ORDER).build();
	}

	public static class A1 {
		@CSVBinding(order = 1)
		private int a;
		@CSVBinding(order = 1)
		private int b;
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = b;
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDuplicateHeader() {
		CSVEngine.builder(A2.class).mode(Mode.NAMED).build();
	}

	public static class A2 {
		@CSVBinding(header="a")
		private int a;
		@CSVBinding(header="a")
		private int b;

		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = b;
		}
	}

	@Test(expected=NullPointerException.class)
	public void testNoConverter() {
		CSVEngine.builder(A3.class).mode(Mode.NAMED).build();
	}

	public static class A3 {
		private A1 a;

		public A1 getA() {
			return a;
		}

		public void setA(A1 a) {
			this.a = a;
		}
	}

	public static class LocalDateFR implements Converter<LocalDate> {
		private final static DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		@Override
		public LocalDate getAsObject(String value) {
			return LocalDate.parse(value, FORMAT);
		}
		@Override
		public String getAsString(LocalDate value) {
			if (value == null) {
				return "";
			}
			try {
				return FORMAT.format(value);
			} catch (Exception e) {
				throw new ConverterException(e);
			}
		}
	}

	@Test
	public void testAutoApply() throws IOException {
		String csv = "31/12/1974,1974-12-31\r\n";
		CSVEngine<A4> engine = CSVEngine.builder(A4.class)
			.register(LocalDate.class, LocalDateFR.class)
			.mode(Mode.ORDER).build();
		List<A4> list = toList(engine.parse(new StringReader(csv), CSVFormat.DEFAULT));
		assertEquals(1, list.size());
		A4 p = list.get(0);
		LocalDate localDate = LocalDate.of(1974,  Month.DECEMBER, 31);
		assertEquals(localDate, p.getLocalDate());
		assertEquals(localDate, p.getLocalDate2());

		StringWriter writer = new StringWriter();
		engine.write(list, writer, CSVFormat.DEFAULT);
		String tmp = writer.toString();
		String csvExpected = csv;
		assertEquals(csvExpected, tmp);
	}

	public static class A4 {
		@CSVBinding(order=0)
		private LocalDate localDate;
		@CSVBinding(order=1)
		@Convert(pattern="yyyy-MM-dd")
		private LocalDate localDate2;

		public LocalDate getLocalDate2() {
			return localDate2;
		}

		public void setLocalDate2(LocalDate localDate2) {
			this.localDate2 = localDate2;
		}


		public LocalDate getLocalDate() {
			return localDate;
		}

		public void setLocalDate(LocalDate localDate) {
			this.localDate = localDate;
		}

	}
}
