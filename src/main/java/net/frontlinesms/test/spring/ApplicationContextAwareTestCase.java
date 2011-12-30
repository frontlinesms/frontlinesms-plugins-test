package net.frontlinesms.test.spring;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import net.frontlinesms.junit.BaseTestCase;

public class ApplicationContextAwareTestCase extends BaseTestCase {
	@Mock protected ApplicationContext ctx;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		initBeans();
	}
	
	private void initBeans() {
		List<Class<?>> classes = new LinkedList<Class<?>>();
		for(Class<?> c = getClass(); c!=ApplicationContextAwareTestCase.class; c = c.getSuperclass()) {
			classes.add(c);
		}
		Collections.reverse(classes);
		for(Class<?> c : classes) initBeans(c);
	}
	
	private void initBeans(Class<?> c) {
		try {
			for(Field f : c.getDeclaredFields()) {
				MockBean a = f.getAnnotation(MockBean.class);
				if(a != null) {
					f.setAccessible(true);
					when(ctx.getBean(f.getName())).thenReturn(f.get(this));
				}
			}
		} catch(Exception ex) { throw new RuntimeException(ex); }
	}
}
