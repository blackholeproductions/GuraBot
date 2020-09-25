package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class SetPrefixCommand extends Command {

	public SetPrefixCommand(String name, String usage, String description) {
		super(name, usage, description);
		this.usablePrivately = false;
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length != 1) {
			SharkUtil.error(message, "You must specify a prefix");
			return;
		}
		ServerInfo.getServerInfo(message.getGuild().getIdLong()).setPrefix(args[0]);
		SharkUtil.success(message, ("Set server prefix to `" + args[0] + "`!"));
	}

}
