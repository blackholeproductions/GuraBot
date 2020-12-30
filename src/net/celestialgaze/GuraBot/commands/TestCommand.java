package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.dv8tion.jda.api.entities.Message;

public class TestCommand extends Command {

	public TestCommand() {
		super(new CommandOptions()
				.setName("test")
				.setDescription("hi")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		
	}

}
