package net.celestialgaze.GuraBot.commands.modules.economy;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SettingsEdit;
import net.celestialgaze.GuraBot.commands.classes.SettingsList;
import net.celestialgaze.GuraBot.commands.classes.SubHelpCommand;
import net.dv8tion.jda.api.Permission;

public class EconomySettings extends SubHelpCommand {

	public EconomySettings(Command parent) {
		super(new CommandOptions("settings", "Manage the settings for the economy module")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsage("<subcommand>")
				.verify(), 
				"Economy Settings", parent);
	}

	@Override
	public void init() {
		SettingsList list = new SettingsList(this);
		subcommands.put(list.getName(), list);
		SettingsEdit edit = new SettingsEdit(this);
		subcommands.put(edit.getName(), edit);
	}

}
