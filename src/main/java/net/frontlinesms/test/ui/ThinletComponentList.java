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
	
	public int count() { return components.length; }
}
