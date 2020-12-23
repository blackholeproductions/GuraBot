package net.celestialgaze.GuraBot.commands;

import java.time.Instant;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Avatar extends Command {

	public Avatar() {
		super(new CommandOptions()
				.setName("avatar")
				.setDescription("Gets the avatar of a user")
				.setUsage("<user>")
				.setCategory("Utility")
				.setUsablePrivate(false)
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		EmbedBuilder eb = new EmbedBuilder()
				.setColor(GuraBot.DEFAULT_COLOR)
				.setTimestamp(Instant.now());
		message.getChannel().sendMessage(new EmbedBuilder().setTitle("Retrieving avatar...").build()).queue(success -> {

			User user = null;
			if (args.length == 0) {
				user = message.getAuthor();
			} else {
				if (message.getChannelType().equals(ChannelType.PRIVATE)) {
					user = SharkUtil.getUser(args, 0);
				} else {
					// First search the guild for the user, then search user cache
					Member member = SharkUtil.getMember(message, args, 0);
					if (member != null) user = member.getUser();
					if (user == null) {
						user = SharkUtil.getUser(args, 0);
					}
				}
				if (user == null) {
					success.editMessage(new EmbedBuilder().setTitle("Unable to determine user.").build()).queue();
					return;
				}
			}
			
			eb.setImage(user.getEffectiveAvatarUrl() + "?size=2048")
			  .setTitle(user.getAsTag());
			success.editMessage(eb.build()).queue();
		});
	}

}
