package net.celestialgaze.GuraBot.commands.modules.xp;

import org.bson.Document;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.modules.xp.leaderboard.XpLeaderboard;
import net.celestialgaze.GuraBot.commands.modules.xp.roles.XpRoles;
import net.celestialgaze.GuraBot.commands.modules.xp.settings.XpSettings;
import net.celestialgaze.GuraBot.commands.modules.xp.toggle.XpToggle;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class Xp extends HelpCommand {

	public Xp() {
		super(new CommandOptions()
				.setName("xp")
				.setDescription("Get the XP you have earned")
				.setUsage("<user>")
				.setCategory("XP")
				.setCooldown(5.0)
				.setUsablePrivate(false)
				.verify(),
				"XP");
	}

	@Override
	public void init() {
		XpHelp help = new XpHelp(this);
		XpToggle toggle = new XpToggle(this);
		XpRoles roles = new XpRoles(this);
		XpLeaderboard leaderboard = new XpLeaderboard(this);
		XpGive give = new XpGive(this);
		XpSettings settings = new XpSettings(this);
		subcommands.put(help.getName(), help);
		subcommands.put(toggle.getName(), toggle);
		subcommands.put(roles.getName(), roles);
		subcommands.put(leaderboard.getName(), leaderboard);
		subcommands.put(give.getName(), give);
		subcommands.put(settings.getName(), settings);
		for (Command cmd : subcommands.values()) {
			commands.put(cmd.getName(), cmd);
		}
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		Member member = SharkUtil.getMember(message, args, 0);
		User user = null;
		if (member == null) {
			user = message.getAuthor();
		} else {
			user = member.getUser();
		}
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		long experience = si.getXP(user.getIdLong(), xpDoc);
		int level = XPUtil.getLevel(experience);
		String roleId = XPUtil.getHighestRole(message.getGuild(), level, xpDoc);
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(message.getGuild().getName(), null, message.getGuild().getIconUrl())
				.setTitle(user.getName())
				.setColor(GuraBot.DEFAULT_COLOR)
				.setThumbnail(user.getEffectiveAvatarUrl())
				.addField("XP", ""+experience, true)
				.addField("Level", ""+level+
						(!roleId.isEmpty() ? " <@&" + roleId + ">" :""), true);

		message.getChannel().sendMessage(eb.build()).queue();
	}
}
