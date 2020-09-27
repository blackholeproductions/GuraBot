package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class Ping extends Command {

	public Ping() {
		super(new CommandOptions()
				.setName("ping")
				.setDescription("Pings the bot and gives you the latency")
				.setCategory("Bot Info")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		long time = System.currentTimeMillis();
		message.getChannel().sendMessage("Pong!")
		.queue(response -> {
			response.editMessage("Pong! Latency is " + (System.currentTimeMillis() - time) + "ms").queue();
		});
	}

}
