package net.frontlinesms.test.ui;

@SuppressWarnings("serial")
public class ComponentNotEnabledException extends RuntimeException {
	public ComponentNotEnabledException(RealThinletComponent component) {
		super("Component not enabled: " + component.getId());
	}
}
