package net.celestialgaze.GuraBot.commands.modules.xp;

import org.bson.Document;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class XpLegacy extends Subcommand {

	public XpLegacy(Command parent) {
		super(new CommandOptions()
				.setName("legacy")
				.setDescription("Get the XP you have earned (old)")
				.setUsage("<user>")
				.setCooldown(5.0)
				.setUsablePrivate(false)
				.verify(), parent);
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
						(!roleId.contentEquals(message.getGuild().getPublicRole().getId()) ? " <@&" + roleId + ">" :""), true);

		message.getChannel().sendMessage(eb.build()).queue();
	}

}
