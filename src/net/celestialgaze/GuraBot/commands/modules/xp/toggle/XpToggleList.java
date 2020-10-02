package net.celestialgaze.GuraBot.commands.modules.xp.toggle;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class XpToggleList extends Subcommand {

	public XpToggleList( Command parent) {
		super(new CommandOptions()
				.setName("list")
				.setDescription("Gets a list of channels black/whitelisted")
				.setUsablePrivate(false)
				.verify(), parent);
		}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		SubDocBuilder sdb = new DocBuilder(xpDoc)
				.getSubDoc("settings")
				.getSubDoc("toggle");
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle((sdb.get("mode", "whitelist").equalsIgnoreCase("whitelist") ? "Whitelisted" : "Blacklisted") + " Channels")
				.setColor(GuraBot.DEFAULT_COLOR);
		
		List<String> greylist = sdb.get("list", new ArrayList<String>());
		String description = "";
		for (String channelID : greylist) {
			if (message.getGuild().getGuildChannelById(channelID) != null) {
				description += "<#"+channelID+">\n";
			}
		}

		if (description.isEmpty()) description = "No channels found\n";
		description += "Current state: **" + (greylist.size() == 0 ? "Disabled" : 
			(sdb.get("mode", "whitelist").equalsIgnoreCase("whitelist") ? "Whitelisted" : "Blacklisted"))+"**";
		
		eb.setDescription(description);
		message.getChannel().sendMessage(eb.build()).queue();
	}

}
