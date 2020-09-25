package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.entities.Message;

public class SimpleCommand extends Command {

	private String response;
	public SimpleCommand(String name, String description, String response) {
		super(name, "", description);
		this.response = response;
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args) {
		message.getChannel().sendMessage(response).queue();
	}
	
}
