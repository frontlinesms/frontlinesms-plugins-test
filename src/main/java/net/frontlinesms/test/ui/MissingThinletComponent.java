package net.frontlinesms.test.ui;

import net.frontlinesms.junit.BaseTestCase;

public class MissingThinletComponent implements ThinletComponent {
	private final String id;
	
	MissingThinletComponent(String id) {
		this.id = id;
	}
	
	public void exists() { fail(); }
	public void click() { fail(); }
	public void close() { fail(); }
	public void select() { fail(); }
	public String getText() { return fail(String.class); }
	public void setText(String v) { fail(); }
	public boolean isEditable() { return fail(boolean.class); }
	public boolean isEnabled() { return fail(boolean.class); }
	public boolean isVisible() { return fail(boolean.class); }
	public boolean isChecked() { return fail(boolean.class); }
	public int getChildCount() { return fail(int.class); }
	public void setSelected(String text) { fail(); }

	private void fail() {
		BaseTestCase.fail("Component missing: " + id);
	}
	
	private <T extends Object> T fail(Class<T> c) {
		BaseTestCase.fail("Component missing: " + id);
		return null;
	}
}
