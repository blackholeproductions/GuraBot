package net.celestialgaze.GuraBot.commands.scc;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;

public class SimpleCmdCreator extends HelpCommand {
	
	public SimpleCmdCreator() {
		super(new CommandOptions()
				.setName("scc")
				.setDescription("Manages simple (text-only responses) commands")
				.setNeedAdmin(true)
				.setCategory("Server")
				.verify(),
				"Simple Command Creator");
		for (Command cmd : subcommands.values()) {
			commands.put(cmd.getName(), cmd);
		}
	}

	@Override
	public void init() {
		Commands.addCommand(this);
		
		// Add subcommands
		SccCreate create = new SccCreate(this);
		SccDelete delete = new SccDelete(this);
		subcommands.put(create.getName(), create);
		subcommands.put(delete.getName(), delete);
		
	}

}
