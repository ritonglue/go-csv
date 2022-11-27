package io.github.ritonglue.gocsv.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a callback method for the corresponding
 * lifecycle event.
 */
@Target({METHOD})
@Retention(RUNTIME)

public @interface PrePersist {}
