package net.frontlinesms.test.ui;

import junit.framework.AssertionFailedError;

public interface ThinletComponent {
//> ACTION METHODS
	/**
	 * Checks if this Thinlet component is real.  If it's not, throws a {@link AssertionFailedError}.
	 * @throws AssertionFailedError
	 */
	public void exists();
	public void click();
	public void doubleClick();
	public void close();
	/** Simulates a click on this item to make it selected TODO double check the method we use and update documentation accordingly */
	public void select();
	/** Specifically for tree nodes. */
	public void expand();
	
//> ACCESSORS
	public String getText();
	public String[] getColumnTitles();
	public void setText(String text);
	public boolean isEnabled();
	public boolean isVisible();
	public boolean isEditable();
	/** for radio buttons and checkboxes */
	public boolean isChecked();
	public boolean isExpanded();
	public boolean isModal();
	public Object getAttachment();
	public void setAttachment(Object attachment);
	public String[] getOptions();
	public int getChildCount();
	public ThinletComponentList getChild();
	public ThinletComponent[] getChildren();
	public ThinletComponent getChild(int index);
	public int getRowCount();
	public ThinletComponent getRow(int index);
	public ThinletComponent[] getRows();
	public String[] getRowText(int index);
	public int getColumnCount();
	public String[] getColumnText(int columnIndex);
	public ThinletComponent getCell(int columnIndex);
	public ThinletComponentList getRootNode();
	public ThinletComponentList getSubNode();
	/**
	 * Set's the selected item by it's text value or attachment. Text
	 * value is preferred, but if no match is found then attachment
	 * will be used.
	 */
	public void setSelected(Object textOrAttachment);
	public ThinletComponent find(String descendantName);
}
