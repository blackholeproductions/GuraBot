package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.entities.Message;

public class PingCommand extends Command {
	
	public PingCommand(String name, String usage, String description) {
		super(name, usage, description);
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args) {
		long time = System.currentTimeMillis();
		message.getChannel().sendMessage("Pong!")
		.queue(response -> {
			response.editMessage("Pong! Latency is " + (System.currentTimeMillis() - time) + "ms").queue();
		});
	}

}
