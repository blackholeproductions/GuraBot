package net.celestialgaze.GuraBot.commands.modules.xp;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class XpHelp extends Subcommand {

	public XpHelp(Command parent) {
		super(new CommandOptions()
				.setName("help")
				.setDescription("All xp subcommands")
				.setUsablePrivate(false)
				.setCooldown(5.0)
				.verify(), parent);
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (parent instanceof HelpCommand) {
			SharkUtil.sendHelpMenu(message, ((HelpCommand)parent));
		}
	}

}
