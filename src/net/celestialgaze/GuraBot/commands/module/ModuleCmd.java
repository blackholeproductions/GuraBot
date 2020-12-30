package net.celestialgaze.GuraBot.commands.module;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;

public class ModuleCmd extends HelpCommand {

	public ModuleCmd() {
		super(new CommandOptions("module", "Manage your server's modules")
				.setUsablePrivate(false)
				.setCategory("Server")
				.verify(),
				"Module");
	}

	@Override
	public void init() {
		ModuleList list = new ModuleList(this);
		ModuleEnable enable = new ModuleEnable(this);
		ModuleDisable disable = new ModuleDisable(this);
		ModuleSettings settings = new ModuleSettings(this);
		subcommands.put(list.getName(), list);
		subcommands.put(enable.getName(), enable);
		subcommands.put(disable.getName(), disable);
		subcommands.put(settings.getName(), settings);
	}

}
