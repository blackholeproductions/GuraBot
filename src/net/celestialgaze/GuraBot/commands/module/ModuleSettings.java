package net.celestialgaze.GuraBot.commands.module;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SubHelpCommand;
import net.celestialgaze.GuraBot.commands.module.settings.ModuleSettingsList;

public class ModuleSettings extends SubHelpCommand {

	/**
	 * @param parent
	 */
	public ModuleSettings(Command parent) {
		super(new CommandOptions()
				.setName("settings")
				.setDescription("Manage the settings of a module")
				.setUsage("\"Module Name\"")
				.setNeedAdmin(true)
				.setUsablePrivate(false)
				.verify(), "Module Settings", parent);
		for (Command cmd : subcommands.values()) {
			commands.put(cmd.getName(), cmd);
		}
	}

	@Override
	public void init() {
		ModuleSettingsList list = new ModuleSettingsList(this);
		subcommands.put(list.getName(), list);
	}


}
