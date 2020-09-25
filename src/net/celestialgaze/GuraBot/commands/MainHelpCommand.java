package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;

public class MainHelpCommand extends HelpCommand {

	public MainHelpCommand(String name, String usage, String description) {
		super(name, usage, description, null, "GuraBot");
	}

	@Override
	public void init() {
		commands = new ArrayList<>(Commands.rootCommands.values());
		for (Command cmd : commands) {
			System.out.println(cmd.getName());
		}
	}
	

}
