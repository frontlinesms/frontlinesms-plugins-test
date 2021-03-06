package net.frontlinesms.test.ui;

import net.frontlinesms.junit.BaseTestCase;

public class MissingThinletComponent implements ThinletComponent {
	private final String id;
	
	MissingThinletComponent(String id) {
		this.id = id;
	}
	
	public void exists() { fail(); }
	public void click() { fail(); }
	public void doubleClick() { fail(); }
	public void close() { fail(); }
	public void select() { fail(); }
	public void expand() { fail(); }
	public String getText() { return fail(String.class); }
	public void setText(String v) { fail(); }
	public String[] getColumnTitles() { return fail(String[].class); }
	public int getColumnCount() { return fail(int.class); }
	public String[] getRowText(int index) { return fail(String[].class); }
	public boolean isEditable() { return fail(boolean.class); }
	public boolean isEnabled() { return fail(boolean.class); }
	public boolean isVisible() { return false; }
	public boolean isChecked() { return fail(boolean.class); }
	public boolean isExpanded() { return fail(boolean.class); }
	public boolean isModal() { return fail(boolean.class); }
	public Object getAttachment() { return fail(Object.class); }
	public void setAttachment(Object attachment) { fail(); }
	public ThinletComponent find(String _) { return fail(ThinletComponent.class); }
	public String[] getOptions() { return fail(String[].class); }
	public ThinletComponentList getChild() { return fail(ThinletComponentList.class); }
	public ThinletComponent getChild(int index) { return fail(ThinletComponent.class); }
	public ThinletComponent[] getChildren() { return fail(ThinletComponent[].class); }
	public ThinletComponentList getRootNode() { return fail(ThinletComponentList.class); }
	public ThinletComponentList getSubNode() { return fail(ThinletComponentList.class); }
	public int getChildCount() { return fail(int.class); }
	public int getRowCount() { return fail(int.class); }
	public ThinletComponent getRow(int index) { return fail(ThinletComponent.class); }
	public ThinletComponent[] getRows() { return fail(ThinletComponent[].class); }
	public String[] getColumnText(int index) { return fail(String[].class); }
	public ThinletComponent getCell(int columnIndex) { return fail(ThinletComponent.class); }
	public void setSelected(Object textOrAttachment) { fail(); }

	private void fail() {
		BaseTestCase.fail("Component missing: " + id);
	}
	
	private <T extends Object> T fail(Class<T> c) {
		BaseTestCase.fail("Component missing: " + id);
		return null;
	}
}
