# GO CSV
A library to parse and write csv files using POJO with Apache commons-csv.
## Table of Contents
1. [Maven Dependency](#maven-dependency)
2. [Quick start](#quick-start)
3. [Converters](#converters)
3. [Date - Time](#date-time)
4. [Life cycle](#life-cycle)
5. [Register converters](#register-converters)

## Maven Dependency
````
<dependency>
  <groupId>io.github.ritonglue</groupId>
  <artifactId>go-csv</artifactId>
  <version>1.0.0</version>
</dependency>
````

## Quick start
You can read or write a CSV file either with Mode.NAMED or with Mode.ORDER. With Mode.NAMED you specify the header.

### Mode.NAMED
You can specify the header or leave it empty.

````
	public class Pojo {

		// header is age
		private Integer age;

		@CSVBinding(header = "the_name")
		private String name;

		//getter, setter, toString
	}
````

````
	public static void main(String[] args) {
		String csv = "the_name;age\r\nJohn;23\r\n";

		try {
			CSVEngine<Pojo> engine = CSVEngine.builder(Pojo.class).mode(Mode.NAMED).build();
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').build();
			List<Pojo> list = engine.parseAsStream(new StringReader(csv), format)
				.collect(Collectors.toList());
			for(Pojo p : list) System.out.println(p);

			System.out.println();

			StringWriter sw = new StringWriter();
			engine.write(list, sw, format);
			System.out.println(sw.toString());
		} catch(IOException e) {
		}
	}
````

### Mode.ORDER
You specify the order (0 based).

````
	public static class Pojo {
		@CSVBinding(order = 1)
		private Integer age;

		@CSVBinding(order = 0)
		private String name;

		//getter, setter, toString
	}
````

````
	public static void main(String[] args) {
		String csv = "John;23\r\n";

		try {
			CSVEngine<Pojo> engine = CSVEngine.builder(Pojo.class).mode(Mode.ORDER).build();
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').build();
			List<Pojo> list = engine.parseAsStream(new StringReader(csv), format)
				.collect(Collectors.toList());
			for(Pojo p : list) System.out.println(p);

			System.out.println();

			StringWriter sw = new StringWriter();
			engine.write(list, sw, format);
			System.out.println(sw.toString());
		} catch(IOException e) {
		}
	}
````

## Converters
Write a converter for complex object

````
	public static class Money {
		private BigDecimal amount;
		private String currency;

		//getters, setters, toString
	}
````

````
	public static class Pojo {
		@Convert(converter = MoneyConverter.class)
		private Money total;

		//getter, setter	
	}
````

````
	public static class MoneyConverter implements Converter<Money> {
		@Override
		public Money getAsObject(String value) {
			if(value == null) return null;
			value = value.strip();
			if(value.isEmpty()) return null;
			String currency = value.substring(0,3);
			BigDecimal amount = new BigDecimal(value.substring(4, value.length()));
			Money money = new Money();
			money.setAmount(amount);
			money.setCurrency(currency);
			return money;
		}

		@Override
		public String getAsString(Money value) {
			return value == null ? null : value.getCurrency() + " " + value.getAmount();
		}
	}
````
````
	public static void main(String[] args) {
		String csv = "total\r\nEUR 1.23\r\n";

		try {
			CSVEngine<Pojo> engine = CSVEngine.builder(Pojo.class).mode(Mode.NAMED).build();
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').build();
			List<Pojo> list = engine.parseAsStream(new StringReader(csv), format)
				.collect(Collectors.toList());
			for(Pojo p : list) System.out.println(p);

			System.out.println();

			StringWriter sw = new StringWriter();
			engine.write(list, sw, format);
			System.out.println(sw.toString());
		} catch(IOException e) {
		}
	}
````

## Date - Time
ISO formats are supported by default. If otherwise, you can use a converter or a pattern.

````
	public static class Pojo {
		private LocalDate dateIso;

		@Convert(pattern="dd/MM/yyyy")
		private LocalDate dateFR;
		
		//registered converter
		@CSVBinding(header="current_time")
		private LocalTime time;

		//getter, setter	
	}
````

````
	public static class LocalTimeConverter implements Converter<LocalTime> {
		@Override
		public LocalTime getAsObject(String value) {
			if(value == null) return null;
			value = value.strip();
			if(value.isEmpty()) return null;
			String hour = value.substring(0,2);
			String minute = value.substring(3,5);
			return LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
		}

		@Override
		public String getAsString(LocalTime value) {
			return value == null ? null : value.getHour() + " " + value.getMinute();
		}
	}
````
````
	public static void main(String[] args) {
		String csv = "dateIso;dateFR;current_time\r\n2022-12-31;31/12/2021;23 59\r\n";

		try {
			CSVEngine<Pojo> engine = CSVEngine.builder(Pojo.class)
				.register(LocalTime.class, LocalTimeConverter.class)
				.mode(Mode.NAMED).build();
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').build();
			List<Pojo> list = engine.parseAsStream(new StringReader(csv), format)
				.collect(Collectors.toList());
			for(Pojo p : list) System.out.println(p);

			System.out.println();

			StringWriter sw = new StringWriter();
			engine.write(list, sw, format);
			System.out.println(sw.toString());
		} catch(IOException e) {
		}
	}
````

## Life cycle
You can use @PostLoad, @PostPersist and @PrePersist annotations to invoke methods after reading, after writing and before writing the pojo.

````
	public class Money {
		private String currency;
		private BigDecimal amount;

		public Money(String currency, BigDecimal amount) {
			this.currency = currency;
			this.amount = amount;
		}
		//getter, setter	
	}
````
````
	public class Pojo {
		@CSVBinding(order = 0)
		//no getter/setter
		private String currency;

		//no getter/setter
		@CSVBinding(order = 1)
		private BigDecimal amount;

		@Transient
		private Money money;

		@PostLoad
		private void afterReading() {
			if(amount != null && currency != null) {
				money = new Money(currency, amount);
			} else {
				money = null;
			}
		}

		@PrePersist
		private void beforeWriting() {
			if(money != null) {
				currency = money.getCurrency();
				amount = money.getAmount();
			} else {
				currency = null;
				amount = null;
			}
		}

		public Money getMoney() {
			return money;
		}

		public void setMoney(Money money) {
			this.money = money;
		}
		//toString
	}
````

````
	public static void main(String[] args) {
		String csv = "currency;amount\r\nEUR;1.23\r\n";

		try {
			CSVEngine<Pojo> engine = CSVEngine.builder(Pojo.class)
				.mode(Mode.NAMED).build();
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').build();
			List<Pojo> list = engine.parseAsStream(new StringReader(csv), format)
				.collect(Collectors.toList());
			for(Pojo p : list) System.out.println(p);

			list.get(0).getMoney().setAmount(new BigDecimal("9.99"));

			System.out.println();

			StringWriter sw = new StringWriter();
			engine.write(list, sw, format);
			System.out.println(sw.toString());
		} catch(IOException e) {
		}
	}
````

## Register converters
You can register converter to auto-apply the conversion : no need to declare a converter at pojo level.

````
	CSVEngine<Pojo> engine = CSVEngine.builder(Pojo.class)
		.register(LocalDate.class, LocalDateFRConverter.class)
		.mode(Mode.NAMED).build();
````