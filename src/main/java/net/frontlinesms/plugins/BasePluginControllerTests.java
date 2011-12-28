/**
 * 
 */
package net.frontlinesms.plugins;

import net.frontlinesms.junit.BaseTestCase;

import static java.util.Locale.UK;

/**
 * @author Alex Anderson
 */
public abstract class BasePluginControllerTests<E extends BasePluginController> extends BaseTestCase {
	E p;
	
	@Override
	protected void setUp() throws Exception {
		this.p = getControllerClass().newInstance();
	}
	
	public void testLoading() {
		assertTrue("Plugin not defined properly in META-INF",
				new PluginImplementationLoader().getAll().contains(getControllerClass()));
	}
	
	public void testGetName() {
		assertNotNull(p.getName(UK));
	}
	
	public void testIconExists() {
		testFileExists("icon", getPropertiesAnnotation().iconPath(), false);
	}
	
	public void testHibernatePropertiesExists() {
		testFileExists("Spring definition", getPropertiesAnnotation().hibernateConfigPath(), true);
	}
	
	public void testSpringContextExists() {
		testFileExists("Spring config", getPropertiesAnnotation().springConfigLocation(), true);
	}
	
	public void testTextBundleLoading() {
		assertNotNull(p.getTextResource());
	}
	
	private void testFileExists(String description, String location, boolean isSpring) {
		if(location.equals(PluginControllerProperties.NO_VALUE)) {
			return;
		}
		String specifiedLocation = location;
		if(isSpring) {
			assertTrue(description + " is not specified as a classpath resource - CHECK THIS IS INTENTIONAL!",
					location.startsWith("classpath:"));
			location = "/" + location.substring("classpath:".length());
		}
		assertNotNull(description + " (" + specifiedLocation + ") could not be found on classpath.",
				p.getClass().getResourceAsStream(location));
	}

	private PluginControllerProperties getPropertiesAnnotation() {
		return getControllerClass().getAnnotation(PluginControllerProperties.class);
	}
	
	protected abstract Class<E> getControllerClass();
}
