package net.celestialgaze.GuraBot.commands.modules.reactroles;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.dv8tion.jda.api.Permission;

public class ReactRole extends HelpCommand {

	public ReactRole() {
		super(new CommandOptions()
				.setName("reactrole")
				.setDescription("Reaction role management")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsage("<subcommand>")
				.setCategory("Server")
				.setUsablePrivate(false)
				.verify(), "Reaction Role");
	}

	@Override
	public void commandInit() {
		ReactRoleGroup group = new ReactRoleGroup(this);
		addSubcommand(group);
	}

}
