package net.celestialgaze.GuraBot.commands.modules.xp;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class XpGive extends Subcommand {

	public XpGive(Command parent) {
		super(new CommandOptions()
				.setName("give")
				.setDescription("Give a user an amount of xp (can be negative)")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsage("<amount> <user>")
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length >= 2) {
			int amount = 0;
			try {
				amount = Integer.parseInt(args[0]);
			} catch (Exception e) {
				SharkUtil.error(message, "You have provided an invalid xp amount");
				return;
			}
			Member member = SharkUtil.getMember(message, args, 1);
			if (member != null) {
				ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
				si.addXP(member.getIdLong(), amount);
				SharkUtil.success(message, "Successfully added " + amount + " XP to " + member.getEffectiveName() + 
						". They now have " + si.getXP(member.getIdLong()) + "xp.");
			} else {
				SharkUtil.error(message, "You have provided an invalid user");
				return;
			}
		} else {
			SharkUtil.error(message, "You have provided an invalid number of arguments");
		}
	}

}
