package net.celestialgaze.GuraBot.commands;

import java.awt.Color;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.ServerInfo;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class HelpCommand extends Command {

	public HelpCommand(String name, String usage, String description) {
		super(name, usage, description);
	}

	@Override
	public void init() {
		Commands.addCommand(this);
	}

	@Override
	public void run(Message message, String[] args) {
		String serverPrefix = (message.getChannelType().equals(ChannelType.TEXT) ? 
				ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix() :
				Commands.defaultPrefix);
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle(GuraBot.jda.getSelfUser().getName() + " Help Menu")
				.setColor(new Color(179, 217, 255));
		for (Command command : Commands.rootCommands.values()) {
			eb.addField(serverPrefix + command.getName() + " " + command.getUsage(), command.getDescription(), false);
		}
		message.getChannel().sendMessage(eb.build()).queue();
	}
	

}
