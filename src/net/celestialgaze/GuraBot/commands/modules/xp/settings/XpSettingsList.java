package net.celestialgaze.GuraBot.commands.modules.xp.settings;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class XpSettingsList extends Subcommand {

	public XpSettingsList(Command parent) {
		super(new CommandOptions()
				.setName("list")
				.setDescription("Lists the settings you can change")
				.setUsablePrivate(false)
				.setPermission(Permission.MANAGE_SERVER)
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		
	}

}
