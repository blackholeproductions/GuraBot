package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class Prefix extends Command {

	public Prefix(String name, String usage, String description) {
		super(name, usage, description);
		this.usablePrivately = false;
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		SharkUtil.info(message, "The prefix for **" + message.getGuild().getName() + "** is `" +
			ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix() + "`.");
	}

}
