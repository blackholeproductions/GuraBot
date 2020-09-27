package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.json.BotInfo;
import net.celestialgaze.GuraBot.json.BotStat;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class Stats extends Command {

	public Stats() {
		super(new CommandOptions()
				.setName("stats")
				.setDescription("Gets some miscellaneous stats")
				.setCategory("Bot Info")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String botInfoList = new BulletListBuilder(true)
				.add("Servers", Integer.toString(GuraBot.jda.getGuilds().size()))
				.add("Uptime", "Online for " + SharkUtil.formatDuration(System.currentTimeMillis() - GuraBot.startDate.getTime()))
				.add("Current Version", GuraBot.version)
				.build();
		String statsList = new BulletListBuilder(true)
				.add("Commands Run", Long.toString(BotInfo.getStat(BotStat.COMMANDS_RUN)))
				.add("Errors", Long.toString(BotInfo.getStat(BotStat.ERRORS)))
				.add("Times Started", Long.toString(BotInfo.getStat(BotStat.STARTS)))
				.build();
		
		message.getChannel().sendMessage(new EmbedBuilder()
				.addField("General Bot Info", botInfoList, false)
				.addField("Stats", statsList, false)
				.build()).queue();
	}

}
