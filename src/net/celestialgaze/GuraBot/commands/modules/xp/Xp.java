package net.celestialgaze.GuraBot.commands.modules.xp;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
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
				.setUsablePrivate(false)
				.verify(),
				"XP");
	}

	@Override
	public void init() {
		XpHelp help = new XpHelp(this);
		subcommands.put(help.getName(), help);
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
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(message.getGuild().getName(), null, message.getGuild().getIconUrl())
				.setTitle(user.getName())
				.setColor(GuraBot.DEFAULT_COLOR)
				.setThumbnail(user.getEffectiveAvatarUrl())
				.addField("XP", ""+si.getXP(user.getIdLong()), true)
				.addField("Level", ""+XPUtil.getLevel(si.getXP(user.getIdLong())), true);

		message.getChannel().sendMessage(eb.build()).queue();
	}
}
