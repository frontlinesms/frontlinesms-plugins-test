/**
 * 
 */
package net.frontlinesms.plugins;

import java.io.File;

import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.plugins.PluginController;

import static java.util.Locale.UK;

/**
 * @author Alex Anderson
 */
public abstract class PluginLoadingTest<E extends PluginController> extends BaseTestCase {
	public void testLoading() throws Exception {
		assertTrue("Plugin not defined properly in META-INF",
				new PluginImplementationLoader().getAll().contains(getControllerClass()));
	}
	
	public void testGetName() throws Exception {
		assertNotNull(getControllerClass().newInstance().getName(UK));
	}
	
	public void testHibernatePropertiesExists() {
		testFileExists("Spring definition", getPropertiesAnnotation().hibernateConfigPath());
	}
	
	public void testSpringContextExists() {
		testFileExists("Spring config", getPropertiesAnnotation().springConfigLocation());
		
	}
	
	private void testFileExists(String description, String location) {
		if(location.equals(PluginControllerProperties.NO_VALUE)) {
			return;
		}
		File f = new File(location);
		assertTrue(description + " does not exist.", f.exists());
		assertTrue(description + " is not a file.", f.isFile());
	}

	private PluginControllerProperties getPropertiesAnnotation() {
		return getControllerClass().getAnnotation(PluginControllerProperties.class);
	}
	
	protected abstract Class<E> getControllerClass();
}
