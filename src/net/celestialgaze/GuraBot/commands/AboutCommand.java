package net.celestialgaze.GuraBot.commands;

import java.awt.Color;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class AboutCommand extends Command {

	public AboutCommand(String name, String usage, String description) {
		super(name, usage, description);
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String selfName = GuraBot.jda.getSelfUser().getName();
		message.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("About " + selfName)
				.setColor(new Color(179, 217, 255))
				.appendDescription("Hello! I'm " + selfName + ", a discord bot by celestialgaze#0001. I'm a re-make " +
				"of an older bot, Uni, created with Java instead of node.js. I hope to regain all of the old " + 
				"functionalities and more, and allow for broad access to *every* feature across *all* areas of Discord. " +
				"A neater, more versatile redesign of Uni. The apex predator of bots.\n\na.")
				.setThumbnail(GuraBot.jda.getSelfUser().getAvatarUrl()).build()).queue();
	}

}
