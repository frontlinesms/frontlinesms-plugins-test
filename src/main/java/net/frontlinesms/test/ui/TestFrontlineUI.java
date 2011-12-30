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
		new BlockingFrontlineUiUpdateJob() {
			public void run() {
				setFocus(component);
				while(!getText(component).isEmpty()) {
					processEvent(new KeyEvent(TestFrontlineUI.this, KeyEvent.KEY_PRESSED, now(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED));
				}
				for(char c : text.toCharArray()) {
					processEvent(new KeyEvent(TestFrontlineUI.this, KeyEvent.KEY_TYPED, now(), 0, KeyEvent.VK_UNDEFINED, c));
				}
			}
		}.execute();
	}
	
	private long now() { return System.currentTimeMillis(); }
}
