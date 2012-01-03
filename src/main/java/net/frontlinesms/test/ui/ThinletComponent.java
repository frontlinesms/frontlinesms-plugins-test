package net.frontlinesms.test.ui;

public interface ThinletComponent {
	public void exists();
	public void click();
	public void close();
	public String getText();
	public void setText(String text);
	public boolean isEnabled();
	public boolean isVisible();
	public int getChildCount();
	public ThinletComponent getSelected();
	public void setSelectedByText(String text);
	public void setSelected(Object selectedComponent);
	public Object getAttachment();
}
