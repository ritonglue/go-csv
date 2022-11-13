package io.github.ritonglue.gocsv.processor;

import java.io.Closeable;
import java.io.IOException;

public interface RecordPrinter extends Closeable {
	void printRecord(Iterable<?> iterable) throws IOException;
}
