package net.frontlinesms.test.ui;

import java.awt.EventQueue;

import net.frontlinesms.ui.events.FrontlineUiUpdateJob;

public abstract class BlockingFrontlineUiUpdateJob extends FrontlineUiUpdateJob {
	public final void execute() {
		try {
			EventQueue.invokeAndWait(this);
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
