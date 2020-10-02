package net.celestialgaze.GuraBot.commands.modules.xp.toggle;

import java.util.ArrayList;
import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class XpToggleClear extends Subcommand {

	public XpToggleClear( Command parent) {
		super(new CommandOptions()
				.setName("clear")
				.setDescription("Clear the list")
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
		// Update
		si.updateModuleDocument("xp", sdb.put("list", new ArrayList<String>()).build());
	}

}
