package net.celestialgaze.GuraBot.commands;

import org.bson.Document;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.db.BotInfo;
import net.celestialgaze.GuraBot.db.BotStat;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class Ping extends Command {

	public Ping() {
		super(new CommandOptions()
				.setName("ping")
				.setDescription("Pings me and gives you the latency")
				.setCategory("Bot Info")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		long time = System.currentTimeMillis();
		message.getChannel().sendMessage(SharkUtil.WAITING_EMBED)
		.queue(response -> {
			long time2 = System.currentTimeMillis();
			BotInfo.addIntStat(BotStat.PINGS);
			long time3 = System.currentTimeMillis();
			response.editMessage(new EmbedBuilder()
					.setTitle("Pong!")
					.setDescription("Latency is " + (System.currentTimeMillis() - time) + "ms (" + GuraBot.jda.getGatewayPing() + "ms gateway)\n" +
					"Database ping: " + (time3-time2) + "ms")
					.setColor(GuraBot.DEFAULT_COLOR)
					.build()).queue();
		});
	}

}
