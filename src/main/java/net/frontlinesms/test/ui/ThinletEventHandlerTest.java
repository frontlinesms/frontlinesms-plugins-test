package net.frontlinesms.test.ui;

import java.awt.event.KeyEvent;
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
		return new RealThinletComponent(rootComponent);
	}
	
	protected ThinletComponent $(String componentName) {
		return findAndCreate(rootComponent, componentName);
	}

	protected class RealThinletComponent implements ThinletComponent {
		private final Object component;
		
		private RealThinletComponent(Object c) {
			if(c instanceof ThinletComponent) throw new IllegalArgumentException("Don't try to wrap multiple " + getClass().getName());
			this.component = c;
		}
		public void click() { ui.invokeAction(component); }
		public void close() { ui.invokeClose(component); }
		public void select() {
			onlyFor(WIDGET_CHECKBOX);
			new BlockingFrontlineUiUpdateJob() {
				public void run() {
					System.out.println(ui.getName(component));
					if(!ui.isSelected(component)) {
						ui.setFocus(component);
						ui.keyChar(' ');
					}					
				}
			}.execute();
		}
		public String getText() { return ui.getText(component); }
		public void setText(String text) {
			if(!isEditable()) fail("Cannot set text on uneditable component.");
			else ui.type(component, text);
		}
		public void exists() {}
		public boolean isEditable() { return ui.getBoolean(component, "editable"); }
		public boolean isChecked() { onlyFor(WIDGET_CHECKBOX); return ui.isSelected(component); }
		public boolean isEnabled() { return ui.isEnabled(component); }
		public boolean isVisible() {
			Object c = component;
			while(ui.getParent(c) != null) c=ui.getParent(c);
			return c == ui.getDesktop();
		}
		public int getChildCount() { return ui.getItems(component).length; }
		public Object getAttachment() { return ui.getAttachedObject(component); }
		public void setSelected(String text) {
			for(Object i : ui.getItems(component)) {
				if(ui.getText(i).equals(text)) {
					if(Thinlet.getClass(component).equals(COMBOBOX)) {
						ui.setText(component, text);
						ui.invokeAction(component);
					} else {
						ui.setSelectedItem(component, i);
						// TODO possibly need to invoke change listener here
					}
					return;
				}
			}
		}
		
		private void onlyFor(String... componentClasses) {
			boolean validClass = false;
			String cc = Thinlet.getClass(component);
			for(String c : componentClasses) {
				if(c.equals(cc)) validClass = true;
			}
			if(!validClass) fail("Cannot apply this method to component of class " + cc);
		}
	}
	
	private ThinletComponent findAndCreate(Object parent, String componentName) {
		Object component = find(parent, componentName);
		if(component == null) component = ui.find(componentName);
		if(component == null) return new MissingThinletComponent(componentName);
		else return new RealThinletComponent(component);
	}
}
