package io.github.ritonglue.gocsv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to specify an access type 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface Access {

	/**
	 * (Required) Specification of field- or property-based access.
	 */
	AccessType value();
}
