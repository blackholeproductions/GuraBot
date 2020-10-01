package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class Prefix extends Command {
	public Prefix() {
		super(new CommandOptions()
				.setName("prefix")
				.setDescription("Gets the current prefix of the server. Usable with the default prefix universally.")
				.setUsablePrivate(false)
				.setCategory("Server")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		SharkUtil.info(message, "The prefix for **" + message.getGuild().getName() + "** is `" +
			ServerInfo.getServerInfo(message.getGuild().getIdLong()).getProperty(ServerProperty.PREFIX, Commands.defaultPrefix) + "`.");
	}

}
