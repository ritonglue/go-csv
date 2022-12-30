package io.github.ritonglue.gocsv.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import io.github.ritonglue.gocsv.annotation.PostLoad;
import io.github.ritonglue.gocsv.annotation.PostPersist;
import io.github.ritonglue.gocsv.annotation.PrePersist;

public enum CallbackEnum {
	  POST_LOAD( PostLoad.class)
	, PRE_PERSIST( PrePersist.class)
	, POST_PERSIST( PostPersist.class)
	;

	private final Class<? extends Annotation> callbackAnnotation;

	private CallbackEnum(Class<? extends Annotation> callbackAnnotation) {
		this.callbackAnnotation = callbackAnnotation;
	}

	public Class<? extends Annotation> getCallbackAnnotation() {
		return callbackAnnotation;
	}

	public boolean hasAnnotation(Method method) {
		return method.getAnnotation(this.callbackAnnotation) != null;
	}
}
