package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.dv8tion.jda.api.entities.Message;

public class RateWaifu extends Command {

	public RateWaifu() {
		super(new CommandOptions()
				.setName("ratewaifu")
				.setDescription("I'll rate your waifu out of ten")
				.setUsage("<waifu>")
				.setCategory("Fun")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		
	}

}
