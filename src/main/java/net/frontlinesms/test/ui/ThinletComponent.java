package net.frontlinesms.test.ui;

public interface ThinletComponent {
//> ACTION METHODS
	public void exists();
	public void click();
	public void close();
	
//> ACCESSORS
	public String getText();
	public void setText(String text);
	public boolean isEnabled();
	public boolean isVisible();
	public boolean isEditable();
	public int getChildCount();
	/** Set's the selected item by it's text value */
	public void setSelected(String text);
}
