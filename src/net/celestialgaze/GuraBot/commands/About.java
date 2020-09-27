package net.celestialgaze.GuraBot.commands;

import java.awt.Color;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class About extends Command {

	public About() {
		super(new CommandOptions()
				.setName("about")
				.setDescription("Information about me, " + GuraBot.jda.getSelfUser().getName()+ "~")
				.setCategory("Bot Info")
				.verify());
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
				"A neater, more versatile redesign of Uni. The apex predator of bots.\n\na. \n[**Invite me to your server!**]"+
				"(https://discord.com/oauth2/authorize?client_id=758355935663095808&permissions=2147483647&scope=bot)")
				.setThumbnail(GuraBot.jda.getSelfUser().getAvatarUrl()).build()).queue();
	}

}
