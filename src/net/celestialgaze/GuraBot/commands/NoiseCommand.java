package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.OpenSimplexNoise;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.dv8tion.jda.api.entities.Message;

public class NoiseCommand extends Command {

	protected NoiseCommand(String name, String usage, String description) {
		super(name, usage, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		Commands.addCommand(this);
	}

	@Override
	public void run(Message message, String[] args) {
		double x = Double.parseDouble(args[0]);
		double y = Double.parseDouble(args[1]);
		double z = Double.parseDouble(args[2]);	
		OpenSimplexNoise noise = new OpenSimplexNoise(192837590);
		if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
			message.getChannel().sendMessage("You have entered invalid numbers.").queue();
			return;
		}
		message.getChannel().sendMessage("Value for " + x + ", " + y + ", " + z + ": " + noise.eval(x, y, z)).queue();
	}

}
