package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class SimpleCommand extends Command {

	public SimpleCommand(Pair<CommandOptions, Boolean> pair, String response) {
		super(pair);
		this.response = response;
	}

	private String response;


	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage(response).queue();
	}
	
}
