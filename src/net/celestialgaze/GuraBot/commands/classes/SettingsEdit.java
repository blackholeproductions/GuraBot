package net.celestialgaze.GuraBot.commands.classes;

import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class SettingsEdit extends Subcommand {
	public SettingsEdit(Command parent) {
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
