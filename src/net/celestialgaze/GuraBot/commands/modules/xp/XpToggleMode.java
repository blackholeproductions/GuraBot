package net.celestialgaze.GuraBot.commands.modules.xp;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class XpToggleMode extends Subcommand {

	public XpToggleMode(Command parent) {
		super(new CommandOptions()
				.setName("mode")
				.setDescription("Set the mode between blacklist and whitelist mode")
				.setUsage("<mode: blacklist, whitelist>")
				.setPermission(Permission.MANAGE_SERVER)
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		
	}

}
