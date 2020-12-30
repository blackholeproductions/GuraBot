package net.celestialgaze.GuraBot.commands.modules.economy;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class EcoGive extends Subcommand {

	public EcoGive(Command parent) {
		super(new CommandOptions("give", "Gives the user the specified amount")
				.setUsage("<amount> <user>")
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length >= 2) {
			EconomyModule eco = EconomyModule.instance;
			Guild guild = message.getGuild();
			long balance = eco.balance.getUserValue(message.getGuild(), message.getAuthor().getIdLong());
			
			long amount = 0;
			
			try {
				amount = Long.parseLong(args[0]);
			} catch (Exception e) {
				SharkUtil.error(message, "You have provided an invalid amount");
				return;
			}
			Member member = SharkUtil.getMember(message, args, 1);
			if (member != null) {
				eco.balance.setUserValue(guild, member.getIdLong(), balance+amount);
				long newBal = eco.balance.getUserValue(guild, member.getIdLong());
				SharkUtil.success(message, "Successfully added `" + amount + "` " + EconomyModule.getEffectiveCurrencyName(guild, amount) +
						" to " + member.getEffectiveName() + ". They now have `" + newBal + "` " + EconomyModule.getEffectiveCurrencyName(guild, newBal));
			} else {
				SharkUtil.error(message, "Unable to find user");
			}
		} else {
			SharkUtil.error(message, "You have provided an invalid number of arguments");
		}
		
	}

}
