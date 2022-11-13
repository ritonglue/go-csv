package io.github.ritonglue.gocsv.processor;

public interface CallbackIndex {
	void missingIndex(AnnotationStorer storer);
	void outOfRangeIndex(AnnotationStorer storer);
}
