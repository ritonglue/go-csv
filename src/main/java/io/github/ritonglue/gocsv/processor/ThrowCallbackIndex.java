package io.github.ritonglue.gocsv.processor;

public class ThrowCallbackIndex implements CallbackIndex {
	@Override
	public void missingIndex(AnnotationStorer storer) {
		String msg = String.format("missing index '%s', header '%s'", storer.getOrder(), storer.getHeader());;
		throw new RuntimeException(msg);
	}

	@Override
	public void outOfRangeIndex(AnnotationStorer storer) {
		String msg = String.format("out of range index '%s', header '%s'", storer.getOrder(), storer.getHeader());;
		throw new RuntimeException(msg);
	}
}
