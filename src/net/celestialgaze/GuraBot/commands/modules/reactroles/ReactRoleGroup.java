package net.celestialgaze.GuraBot.commands.modules.reactroles;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SubHelpCommand;
import net.dv8tion.jda.api.Permission;

public class ReactRoleGroup extends SubHelpCommand {

	public ReactRoleGroup(Command parent) {
		super(new CommandOptions()
				.setName("group")
				.setDescription("Manage reaction role groups")
				.setUsage("<subcommand>")
				.setCategory("Server")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsablePrivate(false)
				.verify(), "Reaction Role Group", parent);
	}

	@Override
	public void commandInit() {
		
	}

}
