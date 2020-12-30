package net.celestialgaze.GuraBot.commands.modules.xp.roles;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SubHelpCommand;
import net.dv8tion.jda.api.Permission;

public class XpRoles extends SubHelpCommand {
	public XpRoles(Command parent) {
		super(new CommandOptions()
				.setName("roles")
				.setDescription("Manage roles given at certain levels")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsablePrivate(false)
				.verify(),
				"XP Roles", 
				parent);
	}

	@Override
	public void commandInit() {
		XpRolesAdd add = new XpRolesAdd(this);
		XpRolesRemove remove = new XpRolesRemove(this);
		XpRolesList list = new XpRolesList(this);
		XpRolesClear clear = new XpRolesClear(this);
		addSubcommand(add);
		addSubcommand(remove);
		addSubcommand(list);
		addSubcommand(clear);
	}
}
