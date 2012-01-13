package net.frontlinesms.test.ui;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import net.frontlinesms.ui.FrontlineUI;

@SuppressWarnings("serial")
public class TestFrontlineUI extends FrontlineUI {
	public TestFrontlineUI() {
		// Fake that the Thinlet UI component has focus.
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
						keyVk(KeyEvent.VK_BACK_SPACE);
					}
					for(char c : text.toCharArray()) {
						keyChar(c);
					}
				}
			}.execute();
		}
	}
	
	public void keyVk(int keyVkThing_givemeapropername) {
		processEvent(new KeyEvent(TestFrontlineUI.this, KeyEvent.KEY_PRESSED, now(), 0, keyVkThing_givemeapropername, KeyEvent.CHAR_UNDEFINED));
	}
	public void keyChar(char keyChar) {
		processEvent(new KeyEvent(TestFrontlineUI.this, KeyEvent.KEY_TYPED, now(), 0, KeyEvent.VK_UNDEFINED, keyChar));
	}
	
	private long now() { return System.currentTimeMillis(); }
}
