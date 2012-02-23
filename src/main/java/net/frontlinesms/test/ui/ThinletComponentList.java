package net.frontlinesms.test.ui;

import java.util.List;

public class ThinletComponentList {
	private final String id;
	private final TestFrontlineUI ui;
	private final Object[] components;
	
	public ThinletComponentList(String id, TestFrontlineUI ui, List<Object> components) {
		this(id, ui, components.toArray());
	}
	
	public ThinletComponentList(String id, TestFrontlineUI ui, Object... components) {
		this.id = id;
		this.ui = ui;
		this.components = components;
	}

	public ThinletComponent withText(String text) {
		String generatedId = id + ".withText('" + text + "')";
		for(Object c : components) {
			if(ui.getText(c).equals(text)) return new RealThinletComponent(generatedId, ui, c);
		}
		return new MissingThinletComponent(generatedId);
	}
	
	public ThinletComponent withIndex(int index) {
		String generatedId = id + "[" + index + "]";
		if(index < components.length)
			return new RealThinletComponent(generatedId, ui, components[index]);
		else return new MissingThinletComponent(generatedId);
	}

	public ThinletComponent withAttachment(Object attachment) {
		String generatedId = id + ".withAttachment('" + attachment + "')";
		for(Object c : components) {
			if(ui.getAttachedObject(c).equals(attachment))
				return new RealThinletComponent(generatedId, ui, c);
		}
		return new MissingThinletComponent(generatedId);
	}
	
	public int count() { return components.length; }
}
