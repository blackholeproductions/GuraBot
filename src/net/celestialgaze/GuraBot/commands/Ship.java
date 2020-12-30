package net.celestialgaze.GuraBot.commands;

import java.util.Random;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class Ship extends Command {

	public Ship() {
		super(new CommandOptions()
				.setName("ship")
				.setDescription("Tells you how much they're destined for true love (or you can rig it)")
				.setUsage("<thing1> <thing2> Optional: (<seed> | --brute-force | --brute-force-lowest)")
				.setCategory("Fun")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length < 2) {
			SharkUtil.error(message, "Please input at least 2 arguments");
			return;
		}

		String ship = (args[0] + args[1]).toLowerCase();
		int seed = SharkUtil.DEFAULT_RANDOM_SEED;
		
		if (modifiers.length > 0) {
			// Brute force modifier
			if (modifiers[0].startsWith("brute-force")) {
				this.cooldownDuration = 2;
				long time = System.currentTimeMillis();
				message.getChannel().sendMessage("Calculating...").queue(response -> {
					boolean lowest = modifiers[0].endsWith("-lowest");
					Random random2 = new Random();
					double highest = (lowest ? 100 : 0);
					int highestSeed = 0;
					for (int i = 0; i < 9999; i++) {
						int seed2 = random2.nextInt(Integer.MAX_VALUE);
						double shipPercent = SharkUtil.randomSeeded(ship, seed2)*100;
						if (!lowest && shipPercent > highest) {
							highest = shipPercent;
							highestSeed = seed2;
						} else if (lowest && shipPercent < highest) {
							highest = shipPercent;
							highestSeed = seed2;
						}
					}
					response.editMessage("Brute-force revealed a result of **" + String.format("%.2f", highest) + "%** with seed `" + 
								highestSeed + "` (" + (System.currentTimeMillis()-time) +"ms)").queue();
				});
				return;
			}
		}
		// Set custom seed
		if (args.length == 3) {
			try {
				seed = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				SharkUtil.error(message, "User Error (you did something wrong): NumberFormatException " + e.getMessage());
				return;
			}
		}
		
		double shipPercent = SharkUtil.randomSeeded(ship, seed)*100;
		String shipPercentString = String.format("%.2f", shipPercent);
		
		message.getChannel().sendMessage(format(args, shipPercentString)).queue();
	}
	
	private String format(String[] args, String percentString) {
		String enclosing1 = (!user(args[0]) ? "`" : "");
		String enclosing2 = (!user(args[1]) ? "`" : "");
		return (enclosing1 + args[0] + enclosing1 + " ♡ " + enclosing2 + args[1] + enclosing2 +": **" + percentString + "%**");
	}
	
	private boolean user(String str) {
		return str.startsWith("<@!") && str.endsWith(">");
	}

}
