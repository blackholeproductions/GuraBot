package net.celestialgaze.GuraBot.commands.modules.counting;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SettingsEdit;
import net.celestialgaze.GuraBot.commands.classes.SettingsList;
import net.celestialgaze.GuraBot.commands.classes.SubHelpCommand;
import net.dv8tion.jda.api.Permission;

public class CountingSettings extends SubHelpCommand {

	public CountingSettings(Command parent) {
		super(new CommandOptions("settings", "Manage the settings for the counting module")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsage("<subcommand>")
				.verify(), 
				"Counting Settings", parent);
	}

	@Override
	public void init() {
		SettingsList list = new SettingsList(this);
		subcommands.put(list.getName(), list);
		SettingsEdit edit = new SettingsEdit(this);
		subcommands.put(edit.getName(), edit);
	}

}
