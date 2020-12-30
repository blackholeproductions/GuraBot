package net.celestialgaze.GuraBot.commands.modules.economy;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.classes.SettingsCmd;
import net.dv8tion.jda.api.Permission;

public class Economy extends HelpCommand {

	public Economy() {
		super(new CommandOptions("eco", "Economy management commands")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsage("<subcommand>")
				.verify(), "Economy");
	}

	@Override
	public void commandInit() {
		SettingsCmd settings = new SettingsCmd(module, this);
		EcoGive give = new EcoGive(this);
		addSubcommand(settings);
		addSubcommand(give);
		
	}

}
