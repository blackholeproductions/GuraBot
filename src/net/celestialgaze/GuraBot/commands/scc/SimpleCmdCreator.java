package net.celestialgaze.GuraBot.commands.scc;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;

public class SimpleCmdCreator extends HelpCommand {

	public SimpleCmdCreator(String name, String usage, String description) {
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
		subcommands.add(new SccCreate(this));
		subcommands.add(new SccDelete(this));
		for (Command cmd : commands) {
			System.out.println(cmd.getName());
		}
	}

}
