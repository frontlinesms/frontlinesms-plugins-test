package net.frontlinesms.test.ui;

import java.util.Collections;

import thinlet.Thinlet;
import net.frontlinesms.test.spring.ApplicationContextAwareTestCase;
import net.frontlinesms.ui.ThinletUiEventHandler;

import static thinlet.Thinlet.*;

public abstract class ThinletEventHandlerTest<E extends ThinletUiEventHandler> extends ApplicationContextAwareTestCase  {
	protected TestFrontlineUI ui;
	/** event handler instance under test */
	protected E h;
	/** root UI component that this handler is controlling */
	private Object rootComponent;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		initUiForTests();
	}
	
	/** Initialise fields of this class for testing.  This is called as part of {@link #setUp()},
	 * but it may be useful to call it again later if e.g. UI needs to be reinitialised after
	 * data mocking/fixtures are in place. */
	protected void initUiForTests() {
		DEFAULT_ENGLISH_BUNDLE = Collections.emptyMap();
		ui = new TestFrontlineUI();
		h = initHandler();
		rootComponent = getRootComponent();
		if(Thinlet.getClass(rootComponent).equals(DIALOG)) {
			ui.add(rootComponent);
		}
	}

	protected abstract E initHandler();
	protected abstract Object getRootComponent();

	protected void waitForUiEvents() {
		new BlockingFrontlineUiUpdateJob() { public void run() {} }.execute();
	}
	
//> UI INTERACTION METHODS/CLASSES
	protected ThinletComponent $() {
		return new RealThinletComponent(getClass().getSimpleName() + ":rootComponent", ui, rootComponent);
	}
	
	/**
	 * Finds a component with the given name.  Any descendant of {@link #rootComponent} will
	 * be prioritised, but if none is found within {@link #rootComponent}, the whole UI will
	 * be searched.
	 * @param componentName
	 * @return
	 */
	protected ThinletComponent $(String componentName) {
		Object component = find(rootComponent, componentName);
		if(component == null) component = ui.find(componentName);
		if(component == null) return new MissingThinletComponent(componentName);
		else return new RealThinletComponent(componentName, ui, component);
	}
}
