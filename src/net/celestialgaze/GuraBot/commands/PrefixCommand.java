package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.ServerInfo;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class PrefixCommand extends Command {

	public PrefixCommand(String name, String usage, String description) {
		super(name, usage, description);
	}

	@Override
	public void init() {
		Commands.addCommand(this);
	}

	@Override
	public void run(Message message, String[] args) {
		if (!message.getChannelType().equals(ChannelType.TEXT)) {
			message.getChannel().sendMessage("This command is not available in private channels.");
			return;
		}
		message.getChannel().sendMessage("The prefix for " + message.getGuild().getName() + " is `" +
			ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix() + "`.").queue();
	}

}
