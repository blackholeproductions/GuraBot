package net.celestialgaze.GuraBot.util;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public abstract class RunnableListener implements Runnable, EventListener {
	public GenericEvent currentEvent;
	
	@Override
	public void onEvent(GenericEvent event) {
		this.currentEvent = event;
		run();
	}
}
