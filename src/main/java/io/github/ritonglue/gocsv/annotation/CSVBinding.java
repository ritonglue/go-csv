package io.github.ritonglue.gocsv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate Methods and fields
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface CSVBinding {

	/**
	 * Determines the header name of the column
	 * @return The value in the csv where of header name is this value.
	 */
	String header() default "";

	/**
	 * Determines the order of the column
	 * In Mode.NAMED, it determines the order of the column during writing.
	 * @return The value on the row of this position.
	 */
	int order() default 0;
}
