package net.frontlinesms.test.ui;

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import net.frontlinesms.ui.FrontlineUI;

@SuppressWarnings("serial")
public class TestFrontlineUI extends FrontlineUI {
	public TestFrontlineUI() {
		// Fake that the Thinlet UI component has focus.
		processEvent(new ComponentEvent(this, ComponentEvent.COMPONENT_SHOWN));
		processEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED));
	}
	
	@Override
	protected void handleException(Throwable t) {
		throw new RuntimeException("Unhandled exception/throwable in UI.", t);
	}
	
	public void invokeClose(final Object component) {
		invoke(component, null, CLOSE);
	}
	
	public void type(final Object component, final String text) {
		if(getBoolean(component, "editable")) {
			new BlockingFrontlineUiUpdateJob() {
				public void run() {
					setFocus(component);
					while(!getText(component).isEmpty()) {
						if(getCaretPosition(component) == 0) {
							keyVk_notThreadsafe(KeyEvent.VK_DELETE);
						} else {
							keyVk_notThreadsafe(KeyEvent.VK_BACK_SPACE);
						}
					}
					for(char c : text.toCharArray()) {
						keyChar_notThreadsafe(c);
					}
				}
			}.execute();
		}
	}
	
	public void keyVk_threadSafe(final int keyVkThing_givemeapropername, final Object focusObject) {
		new BlockingFrontlineUiUpdateJob() {
			public void run() {
				if(focusObject != null) setFocus(focusObject);
				keyVk_notThreadsafe(keyVkThing_givemeapropername);
			}
		}.execute();
	}
	
	public void keyVk_notThreadsafe(int keyVkThing_givemeapropername) {
		processEvent(new KeyEvent(TestFrontlineUI.this, KeyEvent.KEY_PRESSED, now(), 0, keyVkThing_givemeapropername, KeyEvent.CHAR_UNDEFINED));
	}
	
	public void keyChar_threadsafe(final char keyChar, final Object focusObject) {
		new BlockingFrontlineUiUpdateJob() {
			public void run() {
				if(focusObject != null) setFocus(focusObject);
				keyChar_notThreadsafe(keyChar);
			}
		}.execute();
	}
	public void keyChar_notThreadsafe(char keyChar) {
		processEvent(new KeyEvent(TestFrontlineUI.this, KeyEvent.KEY_TYPED, now(), 0, KeyEvent.VK_UNDEFINED, keyChar));
	}
	
	public Object getTree(Object c) {
		return getAncestor(TREE, c);
	}
	
	public Object getAncestor(String componentClass, Object c) {
		while(!getClass(c).equals(componentClass)) {
			c = getParent(c);
		}
		return c;
	}
	
	public void setTreeSelection(Object node) {
		Object tree = getTree(node);
		selectItem(tree, node, true);
	}
	
	public void invokeExpand(Object node) {
		invoke(getTree(node), node, EXPAND);
	}
	
	public void invokePerform(Object component) {
		if(NODE.equals(getClass(component))) {
			invoke(getTree(component), component, PERFORM);
		} else if(ROW.equals(getClass(component))) {
			Object table = getAncestor(TABLE, component);
			setSelectedItem(table, component);
			invoke(table, component, PERFORM);
		} else invoke(component, null, PERFORM);
	}
	
	private long now() { return System.currentTimeMillis(); }
}
