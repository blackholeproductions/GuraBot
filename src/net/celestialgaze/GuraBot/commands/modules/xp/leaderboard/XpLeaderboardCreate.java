package net.celestialgaze.GuraBot.commands.modules.xp.leaderboard;

import java.time.Instant;

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

public class XpLeaderboardCreate extends Subcommand {

	public XpLeaderboardCreate(Command parent) {
		super(new CommandOptions()
				.setName("create")
				.setDescription("Sends a new leaderboard message that will be updated automatically")
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
				.getSubDoc("leaderboard");
		message.getChannel().sendMessage(SharkUtil.WAITING_EMBED).queue(response -> {
			sdb.put("channel", response.getChannel().getIdLong());
			sdb.put("message", response.getIdLong());
			si.updateModuleDocument("xp", sdb.build());
			response.editMessage(si.getLeaderboard(1).setTimestamp(Instant.now()).build()).queue();
		});
	}

}
