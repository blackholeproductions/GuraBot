package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.ServerInfo;
import net.celestialgaze.GuraBot.SharkUtil;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.entities.Message;

public class SetPrefixCommand extends Command {

	public SetPrefixCommand(String name, String usage, String description) {
		super(name, usage, description);
		this.usablePrivately = false;
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args) {
		if (args.length != 1) {
			SharkUtil.error(message, "You must specify a prefix");
			return;
		}
		ServerInfo.getServerInfo(message.getGuild().getIdLong()).setPrefix(args[0]);
		SharkUtil.success(message, ("Set server prefix to `" + args[0] + "`!"));
	}

}
