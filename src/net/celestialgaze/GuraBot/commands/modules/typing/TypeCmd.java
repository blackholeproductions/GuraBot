package net.celestialgaze.GuraBot.commands.modules.typing;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.classes.SettingsCmd;

public class TypeCmd extends HelpCommand {

	public TypeCmd() {
		super(new CommandOptions()
				.setName("type")
				.setDescription("Typing related commands")
				.setCategory("Typing")
				.verify(),
				"Typing");
	}

	@Override
	public void commandInit() {
		TypeTest test = new TypeTest(this);
		TypeLeaderboard leaderboard = new TypeLeaderboard(this);
		SettingsCmd settings = new SettingsCmd(module, this);
		addSubcommand(test);
		addSubcommand(leaderboard);
		addSubcommand(settings);
	}

}
