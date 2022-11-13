package io.github.ritonglue.gocsv.processor;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class RecordPrinterImpl implements RecordPrinter {
	private final CSVPrinter printer;

	public RecordPrinterImpl(Appendable appendable, CSVFormat format) throws IOException {
		this(new CSVPrinter(appendable, format));
	}

	public RecordPrinterImpl(CSVPrinter printer) {
		this.printer = printer;
	}

	@Override
	public void printRecord(Iterable<?> iterable) throws IOException {
		printer.printRecord(iterable);
	}

	@Override
	public void close() throws IOException {
		printer.close();
	}
}
