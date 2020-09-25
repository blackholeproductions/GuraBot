package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.ServerInfo;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.entities.Message;

public class PrefixCommand extends Command {

	public PrefixCommand(String name, String usage, String description) {
		super(name, usage, description);
		this.usablePrivately = false;
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args) {
		message.getChannel().sendMessage("The prefix for " + message.getGuild().getName() + " is `" +
			ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix() + "`.").queue();
	}

}
