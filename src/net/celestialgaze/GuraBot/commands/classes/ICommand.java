package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.entities.Message;

public interface ICommand {
	public void init();
	public void run(Message message, String[] args);
}
