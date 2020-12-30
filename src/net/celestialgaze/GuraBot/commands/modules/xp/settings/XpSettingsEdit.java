package net.celestialgaze.GuraBot.commands.modules.xp.settings;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class XpSettingsEdit extends Subcommand {

	public XpSettingsEdit(Command parent) {
		super(new CommandOptions()
				.setName("edit")
				.setDescription("Edits a setting")
				.setUsage("<setting> <value>")
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length >= 2) {
			if (module.hasSetting(args[0])) {
				CommandModuleSetting<?> setting = module.getSetting(args[0]);
				String input = SharkUtil.toString(args, " ", 1);
				if (setting.trySet(message.getGuild(), input)) {
					SharkUtil.success(message, "Successfully set " + args[0] + " to " + args[1]);
				} else {
					SharkUtil.error(message, setting.getInvalidInputMessage(message.getGuild(), input));
				}
			}
		} else {
			SharkUtil.error(message, "You need to specify a setting and a value to set it to.");
		}
	}

}
