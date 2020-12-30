package net.celestialgaze.GuraBot.commands.modules.economy;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class Balance extends Command {

	public Balance() {
		super(new CommandOptions("bal", "Get the balance of a user")
				.setUsage("<user>")
				.setCategory("Economy")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage(SharkUtil.WAITING_EMBED).queue(sent -> {
			EconomyModule eco = EconomyModule.instance;
			Guild guild = message.getGuild();
			long balance = eco.balance.getUserValue(message.getGuild(), message.getAuthor().getIdLong());
			String currency = EconomyModule.getEffectiveCurrencyName(guild, balance);
			sent.editMessage(new EmbedBuilder().setDescription("You have **" + balance + "** " + currency + ".").build()).queue();
		});
	}

}
