package net.frontlinesms.test.ui;

import static thinlet.ThinletText.*;

import java.util.LinkedList;

import thinlet.Thinlet;

import static net.frontlinesms.test.ui.ThinletEventHandlerTest.*;

public class RealThinletComponent implements ThinletComponent {
	private final String id;
	private final TestFrontlineUI ui;
	private final Object component;
	
	public RealThinletComponent(String id, TestFrontlineUI ui, Object c) {
		if(c instanceof ThinletComponent) throw new IllegalArgumentException("Don't try to wrap multiple " + getClass().getName());
		this.id = id;
		this.ui = ui;
		this.component = c;
	}

//> ThinletComponent METHODS
	public void click() { ui.invokeAction(component); }
	public void doubleClick() { ui.invokePerform(component); }
	public void expand() {
		onlyFor(NODE);
		if(!isExpanded()) {
			select();
			ui.setExpanded(component, true);
			ui.invokeExpand(component);
		}
	}
	public void close() { ui.invokeClose(component); }
	public void select() {
		if(is(WIDGET_CHECKBOX)) { if(!ui.isSelected(component)) ui.keyChar_threadsafe(' ', component); }
		else if(is(NODE)) {
			if(!ui.isSelected(component)) {
				ui.setSelectedItem(ui.getTree(component), component);
				ui.invokeAction(component);
			}
		} else if(is(ROW)) {
			ui.invokeAction(component);
		} else notForThis();
	}
	public String getText() { return ui.getText(component); }
	public void setText(String text) {
		if(!isEditable()) fail("Cannot set text on uneditable component.");
		else ui.type(component, text);
	}
	public String[] getColumnTitles() {
		onlyFor(TABLE);
		Object[] children = ui.getItems(Thinlet.get(component, HEADER));
		int childCount = children.length;
		String[] text = new String[childCount];
		for(int i=0; i<childCount; ++i) {
			text[i] = ui.getText(children[i]);
		}
		return text;
	}
	public void exists() {}
	public boolean isEditable() { return ui.getBoolean(component, "editable"); }
	public boolean isChecked() { onlyFor(WIDGET_CHECKBOX); return ui.isSelected(component); }
	public boolean isEnabled() { return ui.isEnabled(component); }
	public boolean isVisible() {
		if(!ui.getBoolean(component, VISIBLE)) return false;
		Object c = component;
		while(ui.getParent(c) != null) c=ui.getParent(c);
		return c == ui.getDesktop();
	}
	public boolean isExpanded() { onlyFor(NODE); return ui.getBoolean(component, EXPANDED); }
	public boolean isModal() { onlyFor(DIALOG); return ui.getBoolean(component, MODAL); }
	public void setAttachment(Object attachment) { ui.setAttachedObject(component, attachment); }
	public String[] getOptions() {
		onlyFor(COMBOBOX);
		Object[] children = ui.getItems(component);
		String[] options = new String[children.length];
		for (int i = 0; i < options.length; i++) {
			options[i] = ui.getText(children[i]);
		}
		return options;
	}
	public int getChildCount() { return getChild().count(); }
	public int getRowCount() { onlyFor(TABLE); return getChildCount(); }
	public ThinletComponent getRow(int index) { onlyFor(TABLE); return getChild(index); }
	public ThinletComponentList getRootNode() {
		onlyFor(TREE);
		LinkedList<Object> kids = new LinkedList<Object>();
		for(Object o = Thinlet.get(component, ":comp");
				o != null;
				o = ui.getNextItem(component, o, true)) {
			kids.add(o);
		}
		return new ThinletComponentList(id + ".rootNode", ui, kids);
	}
	public ThinletComponentList getSubNode() {
		onlyFor(TREE, NODE);
		LinkedList<Object> kids = new LinkedList<Object>();
		Object tree = ui.getTree(component);
		Object o = component;
		while(true) {
			o = ui.getNextItem(tree, o, true);
			if(o != null) {
				kids.add(o);
			} else break;
		}
		return new ThinletComponentList(id + ".subNode", ui, kids);
	}
	public ThinletComponentList getChild() { notFor(TREE, NODE); return new ThinletComponentList(id + ".child", ui, ui.getItems(component)); }
	public ThinletComponent[] getChildren() {
		Object[] items = ui.getItems(component);
		ThinletComponent[] children = new ThinletComponent[items.length];
		for (int i = 0; i < children.length; i++) {
			children[i] = new RealThinletComponent(id + "child[" + i + "]", ui, items[i]);
		}
		return children;
	}
	public ThinletComponent getChild(int index) { return getChild().withIndex(index); }
	public String[] getRowText(int index) {
		onlyFor(TABLE);
		Object row = ui.getItem(component, index);
		Object[] cells = ui.getItems(row);
		String[] text = new String[cells.length];
		for (int i = 0; i < text.length; i++) {
			text[i] = ui.getText(cells[i]);
		}
		return text;
	}
	public ThinletComponent[] getRows() { onlyFor(TABLE); return getChildren(); }
	public String[] getColumnText(int columnIndex) {
		onlyFor(TABLE);
		ThinletComponent[] rows = getRows();
		String[] columnText = new String[rows.length];
		for (int i = 0; i < columnText.length; i++) {
			ThinletComponent cell = rows[i].getCell(columnIndex);
			columnText[i] = cell instanceof RealThinletComponent?
					cell.getText(): null;
		}
		return columnText;
	}
	public ThinletComponent getCell(int columnIndex) { onlyFor(ROW); return getChild(columnIndex); }
	public Object getAttachment() { return ui.getAttachedObject(component); }
	public void setSelected(String text) {
		if(is(TREE)) {
			getSubNode().withText(text).select();
		} else {
			Object[] items = ui.getItems(component);
			for(int index=0; index<items.length; ++index) {
				Object item = items[index];
				if(ui.getText(item).equals(text)) {
					if(is(COMBOBOX)) {
						ui.setText(component, text);
						ui.setSelectedIndex(component, index);
						ui.invokeAction(component);
					} else {
						ui.setSelectedItem(component, item);
						// TODO possibly need to invoke change listener here
					}
					return;
				}
			}
		}
	}
	public ThinletComponent find(String descendantName) {
		String generatedId = id + ".find('" + descendantName + "')";
		Object descendant = Thinlet.find(component, descendantName);
		if(descendant == null) return new MissingThinletComponent(generatedId);
		else return new RealThinletComponent(generatedId, ui, descendant);
	}
	
//> PRIVATE HELPER METHODS
	private boolean is(String widgetClass) {
		return getComponentClass().equals(widgetClass);
	}
	
	private String getComponentClass() {
		return Thinlet.getClass(component);
	}
	
	private void notFor(String... invalidClasses) {
		if(contains(invalidClasses, getComponentClass())) notForThis();
	}
	
	private void onlyFor(String... validClasses) {
		if(!contains(validClasses, getComponentClass())) notForThis();
	}
	
	private void notForThis() {
		fail("Cannot apply this method to component of class " + getComponentClass());
	}
	
	private boolean contains(String[] haystack, String needle) {
		for(String s : haystack) {
			if(s.equals(needle)) return true;
		}
		return false;
	}
}