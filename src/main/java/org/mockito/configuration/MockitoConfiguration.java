package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import net.frontlinesms.test.spring.MockBean;

import org.mockito.Mock;
import org.mockito.Mockito;

@SuppressWarnings("deprecation")
public class MockitoConfiguration extends DefaultMockitoConfiguration {
	@Override
	public AnnotationEngine getAnnotationEngine() {
		// Shamelessly copied from Mockito's DefaultAnnotationEngine, but with additional
		// mock annotation added.
		return new AnnotationEngine() {
			public Object createMockFor(Annotation annotation, Field field) {
		        if (annotation instanceof Mock
		        		|| annotation instanceof org.mockito.MockitoAnnotations.Mock
		        		|| annotation instanceof MockBean) {
		            return Mockito.mock(field.getType(), field.getName());
		        } else {
		            return null;
		        }
			}
		};
	}
}
