package net.celestialgaze.GuraBot.commands.modules.xp.leaderboard;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SubHelpCommand;
import net.dv8tion.jda.api.Permission;

public class XpLeaderboard extends SubHelpCommand {
	public XpLeaderboard(Command parent) {
		super(new CommandOptions()
				.setName("leaderboard")
				.setDescription("Commands to manage a dedicated auto-updating leaderboard message")
				.setPermission(Permission.MANAGE_SERVER)
				.setUsablePrivate(false)
				.setCooldown(5.0)
				.verify(),
				"XP Leaderboard", 
				parent);
	}

	@Override
	public void commandInit() {
		XpLeaderboardCreate create = new XpLeaderboardCreate(this);
		XpLeaderboardGet get = new XpLeaderboardGet(this);
		addSubcommand(create);
		addSubcommand(get);
	}
}
