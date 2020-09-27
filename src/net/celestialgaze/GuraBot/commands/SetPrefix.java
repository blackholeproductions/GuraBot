package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class SetPrefix extends Command {
	public SetPrefix() {
		super(new CommandOptions()
				.setName("setprefix")
				.setDescription("Sets the prefix of your server")
				.setUsage("<prefix>")
				.setCategory("Server")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length != 1) {
			SharkUtil.error(message, "You must specify a prefix");
			return;
		}
		String prefix = args[0].toLowerCase();
		ServerInfo.getServerInfo(message.getGuild().getIdLong()).setProperty(ServerProperty.PREFIX, prefix);
		SharkUtil.success(message, ("Set server prefix to `" + prefix + "`!"));
	}

}
