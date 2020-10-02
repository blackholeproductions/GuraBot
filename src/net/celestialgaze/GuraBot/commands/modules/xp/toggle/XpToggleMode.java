package net.celestialgaze.GuraBot.commands.modules.xp.toggle;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class XpToggleMode extends Subcommand {

	public XpToggleMode(Command parent) {
		super(new CommandOptions()
				.setName("mode")
				.setDescription("Set the mode between blacklist and whitelist mode, or get the current mode")
				.setUsage("<mode: blacklist, whitelist>")
				.setPermission(Permission.MANAGE_SERVER)
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length < 1) {
			SharkUtil.info(message, "probably something idk lol");
			return;
		}
		if (!args[0].equalsIgnoreCase("blacklist") && !args[0].equalsIgnoreCase("whitelist")) {
			SharkUtil.info(message, SharkUtil.toString(args, " "));
			SharkUtil.error(message, "Invalid input");
			return;
		}
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		si.updateModuleDocument("xp", 
			new DocBuilder(si.getModuleDocument("xp"))
				.getSubDoc("settings")
				.getSubDoc("toggle")
				.put("mode", args[0])
				.build());
		
		SharkUtil.info(message, "Updated to " + args[0] + " mode");
	}

}
