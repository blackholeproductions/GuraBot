package net.celestialgaze.GuraBot.commands.modules.economy;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class Daily extends Command {

	public Daily() {
		super(new CommandOptions("daily", "Claim your daily reward")
				.setCategory("Economy")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage(SharkUtil.WAITING_EMBED).queue(sent -> {
			EconomyModule eco = EconomyModule.instance;
			Guild guild = message.getGuild();
			long user = message.getAuthor().getIdLong();
			long balance = eco.balance.getUserValue(message.getGuild(), user);
			long reward = eco.dailyRewardAmount.get(guild);
			/*
			if (System.currentTimeMillis() - eco.lastDailyClaimed.getUserValue(guild, user).longValue()) {
				
			}
			if (!eco.dailyClaimed.getUserValue(guild, user)) {
				eco.balance.setUserValue(guild, user, balance+reward);
				eco.dailyClaimed.setUserValue(guild, user, true);
				sent.editMessage(new EmbedBuilder()
						.setDescription("Claimed your daily reward of `" + reward + "` " + EconomyModule.getEffectiveCurrencyName(guild, reward) + "! " +
								"Your new balance is `" + (balance+reward) + "`!")
						.build()).queue();
			} else {
				sent.editMessage(new EmbedBuilder()
						.setDescription("You've already claimed your daily reward!")
						.build()).queue();
			}*/
		});
	}


}
