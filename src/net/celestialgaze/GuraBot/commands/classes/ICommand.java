package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.entities.Message;

public interface ICommand {
	public void init();
	void attempt(Message message, String[] args);
	boolean canRun(Message message);
}
