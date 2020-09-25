package net.celestialgaze.GuraBot.commands.scc;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;

public class SimpleCmdCreatorCommand extends HelpCommand {

	public SimpleCmdCreatorCommand(String name, String usage, String description) {
		super(name, usage, description, null, "Simple Command Creator");
		for (Command cmd : subcommands) {
			commands.add(cmd);
		}
		System.out.println(commands == null);
		this.needBotAdmin = true;
	}

	@Override
	public void init() {
		Commands.addCommand(this);
		subcommands.add(new SccCreateCommand(this));
		subcommands.add(new SccDeleteCommand(this));
		for (Command cmd : commands) {
			System.out.println(cmd.getName());
		}
	}

}
