package io.github.ritonglue.gocsv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Specifies the conversion of a field or property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface Convert {

	/**
	 * Specifies the converter to be applied.  A value for this
	 * element must be specified if multiple converters would
	 * otherwise apply.
	 * @return the class converter
	 */
	Class converter() default void.class;

	/**
	 * Specifies the date or time pattern to be applied
	 * @return the date or time pattern
	 */
	String pattern() default "";
}
