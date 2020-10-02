package net.celestialgaze.GuraBot.commands.modules.xp.leaderboard;

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

public class XpLeaderboardGet extends Subcommand {

	public XpLeaderboardGet(Command parent) {
		super(new CommandOptions()
				.setName("get")
				.setDescription("Sends a link to the current leaderboard message")
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
		if (sdb.has("message") && sdb.has("channel")) {
			message.getChannel().sendMessage("https://discordapp.com/channels/" + message.getGuild().getIdLong() + "/" +
					sdb.get("channel", (long)0) + "/" + sdb.get("message", (long)0)).queue();
		} else {
			SharkUtil.info(message, "There is no leaderboard active.");
		}
	}

}
