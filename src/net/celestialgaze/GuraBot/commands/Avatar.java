package net.celestialgaze.GuraBot.commands;

import java.time.Instant;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class Avatar extends Command {

	public Avatar() {
		super(new CommandOptions()
				.setName("avatar")
				.setDescription("Gets the avatar of a user")
				.setUsage("<user>")
				.setCategory("Utility")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		EmbedBuilder eb = new EmbedBuilder()
				.setColor(GuraBot.DEFAULT_COLOR)
				.setTimestamp(Instant.now());
		User user = null;
		if (message.getChannelType().equals(ChannelType.PRIVATE)) {
			PrivateChannel channel = (PrivateChannel) message.getChannel();
			for (Guild g : channel.getUser().getMutualGuilds()) {
				if (user != null) {
					break;
				} else {
					if (SharkUtil.getMember(g, args, 0) != null) user = SharkUtil.getMember(g, args, 0).getUser();
				}
			}
			if (user == null) user = message.getAuthor();
		} else {
			user = (SharkUtil.getMember(message.getGuild(), args, 0) != null ? SharkUtil.getMember(message.getGuild(), args, 0).getUser() : message.getAuthor());
		}
		eb.setImage(user.getEffectiveAvatarUrl() + "?size=2048")
		  .setTitle(user.getAsTag());
		message.getChannel().sendMessage(eb.build()).queue();
	}

}
