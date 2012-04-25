package net.frontlinesms.test.spring;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import net.frontlinesms.junit.BaseTestCase;

public abstract class ApplicationContextAwareTestCase extends BaseTestCase {
	@Mock protected ApplicationContext ctx;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		initBeans();
	}
	
	/**
	 * Initialise {@link #ctx} entries for declared beans for test
	 * class and all superclasses unit
	 * {@link ApplicationContextAwareTestCase} itself is reached.
	 */
	private void initBeans() {
		List<Class<?>> classes = new LinkedList<Class<?>>();
		for(Class<?> c = getClass(); c!=ApplicationContextAwareTestCase.class; c = c.getSuperclass()) {
			classes.add(c);
		}
		Collections.reverse(classes);
		for(Class<?> c : classes) initBeans(c);
	}
	
	/**
	 * Initialise {@link #ctx} entries for declared beans for a
	 * particular class.
	 * @param c
	 */
	private void initBeans(Class<?> c) {
		try {
			List<Field> realFields = new LinkedList<Field>();
			
			for(Field f : c.getDeclaredFields()) {
				if(f.getAnnotation(MockBean.class) != null) {
					f.setAccessible(true);
					when(ctx.getBean(f.getName())).thenReturn(f.get(this));
				} else if(f.getAnnotation(SpringInitialisedBean.class) != null) {
					realFields.add(f);
				}
			}
			
			for(Field f : realFields) {
				Object bean = initRealInstance(f.getType());
				when(ctx.getBean(f.getName())).thenReturn(bean);
				f.setAccessible(true);
				f.set(this, bean);
			}
		} catch(Exception ex) { throw new RuntimeException(ex); }
	}

	private Object initRealInstance(Class<?> c) throws Exception {
		Object bean = c.newInstance();
		for(Field f : c.getDeclaredFields()) {
			if(f.getAnnotation(Autowired.class) != null) {
				f.setAccessible(true);
				Object autowiredValue = ctx.getBean(f.getName());
				f.set(bean, autowiredValue);
			}
		}
		return bean;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public <T> Answer returnMockBean() {
		return new Answer<T>() {
			public T answer(InvocationOnMock invocation) throws Throwable {
				String beanName = (String) invocation.getArguments()[0];
				return (T) ctx.getBean(beanName);
			}
		};
	}
}
