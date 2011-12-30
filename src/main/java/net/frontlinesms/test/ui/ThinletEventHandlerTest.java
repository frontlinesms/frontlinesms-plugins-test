package net.frontlinesms.test.ui;

import java.util.Collections;

import thinlet.Thinlet;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.ui.ThinletUiEventHandler;

public abstract class ThinletEventHandlerTest<E extends ThinletUiEventHandler> extends BaseTestCase  {
	protected TestFrontlineUI ui;
	/** event handler instance under test */
	protected E h;
	/** root UI component that this handler is controlling */
	private Object rootComponent;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Thinlet.DEFAULT_ENGLISH_BUNDLE = Collections.emptyMap();
		ui = new TestFrontlineUI();
		h = initHandler();
		rootComponent = getRootComponent();
		ui.add(rootComponent);
	}

	protected abstract E initHandler();
	protected abstract Object getRootComponent();
	
//> UI INTERACTION METHODS/CLASSES
	protected ThinletComponent $() {
		return new RealThinletComponent(rootComponent);
	}
	
	protected ThinletComponent $(String componentName) {
		return create(rootComponent, componentName);
	}

	protected class RealThinletComponent implements ThinletComponent {
		private final Object component;
		
		private RealThinletComponent(Object c) {
			if(c instanceof ThinletComponent) throw new IllegalArgumentException("Don't try to wrap multiple " + getClass().getName());
			this.component = c;
		}
		public void click() { ui.invokeAction(component); }
		public void close() { ui.invokeClose(component); }
		public String getText() { return ui.getText(component); }
		public void setText(String text) { ui.type(component, text); }
		public void exists() {}
		public boolean isEnabled() { return ui.isEnabled(component); }
		public boolean isVisible() {
			Object c = component;
			while(ui.getParent(c) != null) c=ui.getParent(c);
			return c == ui.getDesktop();
		}
	}
	
	private ThinletComponent create(Object parent, String componentName) {
		Object component = Thinlet.find(parent, componentName);
		if(component == null) component = ui.find(componentName);
		if(component == null) return new MissingThinletComponent(componentName);
		else return new RealThinletComponent(component);
	}
}
