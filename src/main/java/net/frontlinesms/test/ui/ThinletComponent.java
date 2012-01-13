package net.frontlinesms.test.ui;

public interface ThinletComponent {
//> ACTION METHODS
	public void exists();
	public void click();
	public void close();
	/** Simulates a click on this item to make it selected TODO double check the method we use and update documentation accordingly */
	public void select();
	
//> ACCESSORS
	public String getText();
	public void setText(String text);
	public boolean isEnabled();
	public boolean isVisible();
	public boolean isEditable();
	/** for radio buttons and checkboxes */
	public boolean isChecked();
	public int getChildCount();
	/** Set's the selected item by it's text value */
	public void setSelected(String text);
}
