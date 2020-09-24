package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.OpenSimplexNoise;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.entities.Message;

public class ShipCommand extends Command {
	public ShipCommand(String name, String usage, String description) {
		super(name, usage, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		Commands.addCommand(this);
	}

	@Override
	public void run(Message message, String[] args) {
		String ship = (args[0] + args[1]).toLowerCase();
		OpenSimplexNoise noise = new OpenSimplexNoise();
		int x = 0, y = 0;
		for (int i = 0; i < ship.length(); i++) {
			int value = Math.toIntExact(Math.round(toPercent(noise.eval(ship.charAt(i)*100, 10))));
			if (i % 2 == 0) {
				x += value;
			} else {
				y += value;
			}
		}
		double shipPercent = toPercent(noise.eval(x, y));
		String shipPercentString = String.format("%.2f", shipPercent);

		for (int i = 0; i < 2; i++) {
			if (args[i].startsWith("<@!") && args[i].endsWith(">")) {
				args[i] = args[i].substring(3, args[i].indexOf(">")-1);
			}
		}
		message.getChannel().sendMessage("`" + args[0] + "` ❤︎ `" + args[1] + "`: **" + shipPercentString + "%**").queue();
	}
	private double toPercent(double noiseValue) {
		return (noiseValue+1)*50; // starts w/ range from -1 to 1. +1 changes it to from 0 to 2, x50 changes it from 0 to 100
	}
	private String format(String[] args, String percentString) {
		return ("`" + args[0] + "`" + "❤︎" + "`" + args[1] + "`: **" + percentString + "%**");
	}
	private bool user(String str) {
		return str.startsWith("<@!") && str.endsWith(">");
	}

}
