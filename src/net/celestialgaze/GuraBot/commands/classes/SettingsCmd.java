package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.Permission;

public class SettingsCmd extends SubHelpCommand {
	public SettingsCmd(CommandModule module, Command parent) {
		super(new CommandOptions()
				.setName("settings")
				.setDescription("Manage settings of the " + module.getName() + " module")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsablePrivate(false)
				.verify(),
				"XP Settings", 
				parent);
	}

	@Override
	public void commandInit() {
		SettingsList list = new SettingsList(this);
		SettingsEdit edit = new SettingsEdit(this);
		addSubcommand(list);
		addSubcommand(edit);
		
	}
}
