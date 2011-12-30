package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import net.frontlinesms.test.spring.MockBean;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.configuration.DefaultAnnotationEngine;

public class MockitoConfiguration extends DefaultMockitoConfiguration {
	@Override
	public AnnotationEngine getAnnotationEngine() {
		// Shamelessly copied from Mockito's DefaultAnnotationEngine, but with additional
		// mock annotation added.
		return new DefaultAnnotationEngine() {
			public Object createMockFor(Annotation annotation, Field field) {
		        if (annotation instanceof MockBean) {
		            return Mockito.mock(field.getType(), field.getName());
		        } else return super.createMockFor(annotation, field);
			}
		};
	}
}
