package net.celestialgaze.GuraBot.commands.modules.counting;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.commands.classes.settings.ChannelIDSetting;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class CountingSetChannel extends Subcommand {

	public CountingSetChannel(Command parent) {
		super(new CommandOptions("setchannel", "Set the counting channel")
				.setUsage("<channel>")
				.setPermission(Permission.MANAGE_SERVER)
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		ChannelIDSetting setting = CountingModule.instance.channel;
		String input = SharkUtil.toString(args, " ");
		Guild guild = message.getGuild();
		if (setting.trySet(guild, input)) {
			SharkUtil.success(message, "Successfully set counting channel to " + input);
		} else {
			SharkUtil.error(message, setting.getInvalidInputMessage(guild, input));
		}
	}

}
