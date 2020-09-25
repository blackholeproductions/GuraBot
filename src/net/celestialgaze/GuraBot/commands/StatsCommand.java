package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.json.BotInfo;
import net.celestialgaze.GuraBot.json.StatType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class StatsCommand extends Command {

	public StatsCommand(String name, String usage, String description) {
		super(name, usage, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage(new EmbedBuilder()
				.addField("Servers", Integer.toString(GuraBot.jda.getGuilds().size()), false)
				.addField("Commands Run", Long.toString(BotInfo.getStat(StatType.COMMANDS_RUN)), false)
				.addField("Errors", Long.toString(BotInfo.getStat(StatType.ERRORS)), false)
				.addField("Times Started", Long.toString(BotInfo.getStat(StatType.STARTS)), false)
				.build()).queue();
	}

}
