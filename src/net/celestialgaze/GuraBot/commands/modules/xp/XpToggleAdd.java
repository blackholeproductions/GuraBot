package net.celestialgaze.GuraBot.commands.modules.xp;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class XpToggleAdd extends Subcommand {

	public XpToggleAdd(Command parent) {
		super(new CommandOptions()
				.setName("add")
				.setDescription("Adds the channel you run this command in to the list of channels black/whitelisted")
				.setUsablePrivate(false)
				.setPermission(Permission.MANAGE_SERVER)
				.verify(), parent);
		}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		SubDocBuilder sdb = new DocBuilder(xpDoc)
				.getSubDoc("settings")
				.getSubDoc("toggle");
		
		List<String> greylist = sdb.get("list", new ArrayList<String>());
		String channelStrID = Long.toString(message.getChannel().getIdLong());
		if (!greylist.contains(channelStrID)) {
			greylist.add(channelStrID);
			si.updateModuleDocument("xp", sdb.put("list", greylist).build());
			SharkUtil.info(message, "Added <#"+message.getChannel().getIdLong()+"> to list");
			return;
		}
		SharkUtil.info(message, "<#"+message.getChannel().getIdLong()+"> was already in the list");
	}

}
