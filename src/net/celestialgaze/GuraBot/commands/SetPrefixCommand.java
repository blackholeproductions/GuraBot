package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.ServerInfo;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.entities.Message;

public class SetPrefixCommand extends Command {

	public SetPrefixCommand(String name, String usage, String description) {
		super(name, usage, description);
	}

	@Override
	public void init() {
		Commands.addCommand(this);
	}

	@Override
	public void run(Message message, String[] args) {
		ServerInfo.getServerInfo(message.getGuild().getIdLong()).setPrefix(args[0]);
		message.getChannel().sendMessage("Set server prefix to `" + args[0] + "`!").queue();
	}

}
