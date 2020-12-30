package net.celestialgaze.GuraBot.commands.modules.counting;

import java.util.List;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.commands.classes.settings.PositiveIntegerSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.UserIDSetting;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CountingFix extends Subcommand {

	public CountingFix(Command parent) {
		super(new CommandOptions("fix", "Read the latest message in the counting channel and fix the count")
				.setPermission(Permission.MANAGE_SERVER)
				.setCooldown(30.0)
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		TextChannel countChannel = message.getGuild().getTextChannelById(CountingModule.instance.channel.get(message.getGuild()));
		Guild guild = message.getGuild();
		PositiveIntegerSetting current = CountingModule.instance.current;
		UserIDSetting currentUser = CountingModule.instance.currentUser;
		List<Message> messages = countChannel.getHistory().retrievePast(1).complete();
		if (messages.size() > 0) {
			Message latest = messages.get(0);
			current.trySet(guild, latest.getContentRaw());
			currentUser.set(guild, latest.getMember().getIdLong());
			SharkUtil.success(message, "Set count to `" + current.displayCurrent(guild) + "` by user " + currentUser.displayCurrent(guild));
		} else {
			CountingModule.instance.current.restore(guild, false);
			CountingModule.instance.currentUser.restore(guild, false);
			SharkUtil.success(message, "Reset count to 0");
		}
	}
	
}
