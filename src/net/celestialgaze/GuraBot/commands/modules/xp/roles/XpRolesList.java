package net.celestialgaze.GuraBot.commands.modules.xp.roles;

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

public class XpRolesList extends Subcommand {

	public XpRolesList( Command parent) {
		super(new CommandOptions()
				.setName("list")
				.setDescription("Gets a list of roles and their levels")
				.setUsablePrivate(false)
				.verify(), parent);
		}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		SubDocBuilder sdb = new DocBuilder(xpDoc)
				.getSubDoc("settings");
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("XP Roles")
				.setColor(GuraBot.DEFAULT_COLOR);
				
		Document roles = sdb.getSubDoc("roles").buildThis();
		String description = "";
		List<String> rolesKeys = new ArrayList<String>(roles.keySet());
		for (int i = 0; i < roles.size(); i++) {
			String level = rolesKeys.get(i);
			String roleId = roles.getString(level);
			description += "**Level " + level + "**: <@&" + roleId + ">";
		}
		if (description.isEmpty()) description = "No roles  found\n";

		eb.setDescription(description);
		message.getChannel().sendMessage(eb.build()).queue();
	}

}
