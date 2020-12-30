package net.celestialgaze.GuraBot.commands.modules.scc;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.dv8tion.jda.api.Permission;

public class CommandCreator extends HelpCommand {

	public CommandCreator() {
		super(new CommandOptions("cc", "Create custom commands for your server")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsablePrivate(false)
				.setUsage("<subcommand>")
				.verify(), "Command Creator");
	}

	@Override
	public void commandInit() {
		CcCreate create = new CcCreate(this);
		CcDelete delete = new CcDelete(this);
		addSubcommand(create);
		addSubcommand(delete);
		
	}

}
