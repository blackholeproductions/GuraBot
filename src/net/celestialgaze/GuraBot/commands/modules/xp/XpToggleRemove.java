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

public class XpToggleRemove extends Subcommand {

	public XpToggleRemove( Command parent) {
		super(new CommandOptions()
				.setName("remove")
				.setDescription("Remove the channel you run this command from the list of channels black/whitelisted")
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
		// Get list
		List<String> greylist = sdb.get("list", new ArrayList<String>());
		// Add current channel
		String channelStrID = Long.toString(message.getChannel().getIdLong());
		if (greylist.contains(channelStrID)) {
			greylist.remove(channelStrID);
			// Update
			si.updateModuleDocument("xp", sdb.put("list", greylist).build());
			SharkUtil.info(message, "Removed <#"+message.getChannel().getIdLong()+"> from list");
			return;
		}
		SharkUtil.info(message, "<#"+message.getChannel().getIdLong()+"> wasn't in the list");
	}

}
