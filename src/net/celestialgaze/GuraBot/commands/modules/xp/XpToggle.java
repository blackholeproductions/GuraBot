package net.celestialgaze.GuraBot.commands.modules.xp;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.dv8tion.jda.api.Permission;

public class XpToggle extends HelpCommand {

	public XpToggle() {
		super(new CommandOptions()
				.setName("toggle")
				.setDescription("Manages which channels xp is enabled in")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsablePrivate(false)
				.verify(), 
				"XP List");
	}

	@Override
	public void init() {
		
	}

}
