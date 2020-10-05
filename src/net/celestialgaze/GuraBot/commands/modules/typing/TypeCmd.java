package net.celestialgaze.GuraBot.commands.modules.typing;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;

public class TypeCmd extends HelpCommand {

	public TypeCmd() {
		super(new CommandOptions()
				.setName("type")
				.setDescription("Typing related commands")
				.setCategory("Typing")
				.verify(),
				"Typing");
		for (Command cmd : subcommands.values()) {
			commands.put(cmd.getName(), cmd);
		}
	}

	@Override
	public void init() {
		TypeTest test = new TypeTest(this);
		TypeLeaderboard leaderboard = new TypeLeaderboard(this);
		subcommands.put(test.getName(), test);
		subcommands.put(leaderboard.getName(), leaderboard);
	}

}
