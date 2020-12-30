package net.celestialgaze.GuraBot.commands.module;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.module.settings.ModuleSettings;

public class ModuleCmd extends HelpCommand {

	public ModuleCmd() {
		super(new CommandOptions("module", "Manage your server's modules")
				.setUsablePrivate(false)
				.setCategory("Server")
				.verify(),
				"Module");
	}

	@Override
	public void commandInit() {
		ModuleList list = new ModuleList(this);
		ModuleEnable enable = new ModuleEnable(this);
		ModuleDisable disable = new ModuleDisable(this);
		ModuleSettings settings = new ModuleSettings(this);
		addSubcommand(list);
		addSubcommand(enable);
		addSubcommand(disable);
		addSubcommand(settings);
	}

}
