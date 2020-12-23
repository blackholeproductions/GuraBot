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
		for (Command cmd : subcommands.values()) {
			commands.put(cmd.getName(), cmd);
		}
	}

	@Override
	public void init() {
		ReactRoleGroup group = new ReactRoleGroup(this);
		subcommands.put(group.getName(), group);
	}

}
