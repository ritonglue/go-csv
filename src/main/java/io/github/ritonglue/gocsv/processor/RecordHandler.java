package io.github.ritonglue.gocsv.processor;

@FunctionalInterface
public interface RecordHandler {
	/**
	 * Handler to modify the object before processing
	 * @param value the original value
	 * @param object the converted value
	 * @param storer annotation information
	 * @return the object that will be processed
	 */
	Object accept(String value, Object object, AnnotationStorer storer);
}
