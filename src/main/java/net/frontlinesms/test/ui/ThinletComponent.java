package net.frontlinesms.test.ui;

public interface ThinletComponent {
//> ACTION METHODS
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
	public void setText(String text);
	public boolean isEnabled();
	public boolean isVisible();
	public boolean isEditable();
	/** for radio buttons and checkboxes */
	public boolean isChecked();
	public boolean isExpanded();
	public int getChildCount();
	public ThinletComponentList getChild();
	public ThinletComponentList getRootNode();
	public ThinletComponentList getSubNode();
	/** Set's the selected item by it's text value */
	public void setSelected(String text);
	public ThinletComponent find(String descendantName);
}
