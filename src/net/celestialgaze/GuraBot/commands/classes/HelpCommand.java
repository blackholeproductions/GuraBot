package net.celestialgaze.GuraBot.commands.classes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public abstract class HelpCommand extends Command {

	protected List<Command> commands = new ArrayList<Command>();
	protected String helpMenuName = "";
	protected HelpCommand(String name, String usage, String description, List<Command> commands, String helpMenuName) {
		super(name, usage, description, false);
		if (commands != null) {
			for (Command cmd : commands) {
				this.commands.add(cmd);
			}
		}
		this.helpMenuName = helpMenuName;
		init();
	}
	
	public abstract void init();

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String serverPrefix = (message.getChannelType().equals(ChannelType.TEXT) ? 
				ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix() :
				Commands.defaultPrefix);
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle(helpMenuName + " Help Menu")
				.setColor(GuraBot.DEFAULT_COLOR);
		for (Command command : commands) {
			if (command.canRun(message))
				eb.addField(serverPrefix + (commands.equals(subcommands) ? this.getName() + " " : "") +  command.getName() + " " + command.getUsage(), command.getDescription(), false);
		}
		message.getChannel().sendMessage(eb.build()).queue();
	}

}
