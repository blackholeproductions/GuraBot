package net.celestialgaze.GuraBot.commands.modules.xp.settings;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SubHelpCommand;
import net.dv8tion.jda.api.Permission;

public class XpSettings extends SubHelpCommand {
	public XpSettings(Command parent) {
		super(new CommandOptions()
				.setName("settings")
				.setDescription("Manage settings of the XP module")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsablePrivate(false)
				.verify(),
				"XP Settings", 
				parent);
		for (Command cmd : subcommands.values()) {
			commands.put(cmd.getName(), cmd);
		}
	}

	@Override
	public void init() {
		XpSettingsList list = new XpSettingsList(this);
		subcommands.put(list.getName(), list);
		XpSettingsEdit edit = new XpSettingsEdit(this);
		subcommands.put(edit.getName(), edit);
		
	}
}
