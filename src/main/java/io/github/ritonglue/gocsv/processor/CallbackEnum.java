package io.github.ritonglue.gocsv.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.github.ritonglue.gocsv.annotation.PostLoad;
import io.github.ritonglue.gocsv.annotation.PostPersist;
import io.github.ritonglue.gocsv.annotation.PrePersist;

public enum CallbackEnum {
	  POST_LOAD( PostLoad.class, "javax.persistence.PostLoad", "jakarta.persistence.PostLoad")
	, PRE_PERSIST( PrePersist.class, "javax.persistence.PrePersist", "jakarta.persistence.PrePersist" )
	, POST_PERSIST( PostPersist.class, "javax.persistence.PostPersist", "jakarta.persistence.PostPersist" )
	;

	private final Class<? extends Annotation> callbackAnnotation;
	private final List<Class<? extends Annotation>> annotations = new ArrayList<>();

	private CallbackEnum(Class<? extends Annotation> callbackAnnotation, String... others) {
		this.callbackAnnotation = callbackAnnotation;
		annotations.add(callbackAnnotation);
		for(String o : others) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends Annotation> clazz = (Class<? extends Annotation>) Class.forName(o);
				annotations.add(clazz);
			} catch(ClassNotFoundException e) {
				//ignore
			}
		}
	}

	public Class<? extends Annotation> getCallbackAnnotation() {
		return callbackAnnotation;
	}

	public boolean hasAnnotation(Method method) {
		for(Class<? extends Annotation> a : annotations) {
			if(method.getAnnotation(a) != null) {
				return true;
			}
		}
		return false;
	}
}
