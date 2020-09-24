package net.celestialgaze.GuraBot.commands;

import java.util.Random;

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
		Random random = new Random();
		int x = 0, y = 0;
		for (int i = 0; i < ship.length(); i++) {
			int value = Math.toIntExact(Math.round(toPercent(noise.eval(ship.charAt(i)*100, 10))));
			if (i % 2 == 0) {
				x += value;
			} else {
				y += value;
			}
		}
		random.setSeed(Math.toIntExact(Math.round((noise.eval(x, y)*Integer.MAX_VALUE))));
		double shipPercent = random.nextDouble()*100;
		String shipPercentString = String.format("%.2f", shipPercent);
		
		message.getChannel().sendMessage(format(args, shipPercentString)).queue();
	}
	
	private double toPercent(double noiseValue) {
		return (noiseValue+1)*50; // starts w/ range from -1 to 1. +1 changes it to from 0 to 2, x50 changes it from 0 to 100
	}
	
	private String format(String[] args, String percentString) {
		String enclosing1 = (!user(args[0]) ? "`" : "");
		String enclosing2 = (!user(args[1]) ? "`" : "");
		return (enclosing1 + args[0] + enclosing1 + " ❤︎ " + enclosing2 + args[1] + enclosing2 +": **" + percentString + "%**");
	}
	
	private boolean user(String str) {
		return str.startsWith("<@!") && str.endsWith(">");
	}

}
