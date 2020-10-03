package net.celestialgaze.GuraBot.commands;

import java.time.Instant;
import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class UserInfo extends Command {

	public UserInfo() {
		super(new CommandOptions()
				.setName("user")
				.setDescription("Gets information about a user")
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
		User user = null;
		if (message.getChannelType().equals(ChannelType.PRIVATE)) {
			user = message.getAuthor();
		} else {
			user = (SharkUtil.getMember(message, args, 0) != null ? SharkUtil.getMember(message, args, 0).getUser() : message.getAuthor());
		}
		String description = new BulletListBuilder()
				.add("**ID**", user.getId())
				.add("**Account Created**", user.getTimeCreated().toString().replace("T", " ").replace("Z", ""))
				.build();
		eb.setThumbnail(user.getEffectiveAvatarUrl() + "?size=2048")
		  .setTitle(user.getAsTag())
		  .setDescription(description);
		message.getChannel().sendMessage(eb.build()).queue();
	}

}
