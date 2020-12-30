package net.celestialgaze.GuraBot.commands.modules.economy;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.dv8tion.jda.api.Permission;

public class Economy extends HelpCommand {

	public Economy() {
		super(new CommandOptions("eco", "Economy management commands")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsage("<subcommand>")
				.verify(), "Economy");
	}

	@Override
	public void init() {
		EconomySettings settings = new EconomySettings(this);
		EcoGive give = new EcoGive(this);
		subcommands.put(settings.getName(), settings);
		subcommands.put(give.getName(), give);
		
	}

}
