package net.celestialgaze.GuraBot.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.ImageBuilder;
import net.celestialgaze.GuraBot.util.OpenSimplexNoise;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class Noise extends Command {
	
	public Noise() {
		super(new CommandOptions()
				.setName("noise")
				.setDescription("Gets OpenSimplex noise value at <x>, <y>, <z>.")
				.setUsage("<x> <y> <z>")
				.setCategory("Debug")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length < 3) {
			SharkUtil.error(message, "You must provide all 3 arguments");
			return;
		}
		double x = Double.parseDouble(args[0]);
		double y = Double.parseDouble(args[1]);
		double z = Double.parseDouble(args[2]);
		
		OpenSimplexNoise noise = new OpenSimplexNoise(192837590);
		if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
			message.getChannel().sendMessage("You have entered invalid numbers.").queue();
			return;
		}
		final int WIDTH = 512;
		final int HEIGHT = 512;
		final int FEATURE_SIZE = 1;
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < HEIGHT; i++)
		{
			for (int j = 0; j < WIDTH; j++)
			{
				double value;
				try {
					value = noise.eval(((toInt(x)-HEIGHT/2)+j)/FEATURE_SIZE, ((toInt(y)-HEIGHT/2)+i)/FEATURE_SIZE, z);
				} catch (ArithmeticException e) {
					message.getChannel().sendMessage("Error while calculating noise: " + e.getMessage()).queue();
					return;
				}
				int rgb = 0x010101 * (int)((value + 1) * 127.5);
				image.setRGB(i, j, rgb);
			}
		}
		try {
			message.getChannel().sendFile(new ImageBuilder(image).build().toByteArray(), "noise.png").embed(new EmbedBuilder()
					.setImage("attachment://noise.png")
					.setColor(0x010101 * (int) ((noise.eval(x/FEATURE_SIZE, y/FEATURE_SIZE, z)+1)*127.5))
					.setDescription("Value for " + x + ", " + y + ", " + z + ": " + noise.eval(x/FEATURE_SIZE, y/FEATURE_SIZE, z))
					.build()).queue();
		} catch (IOException e) {
			e.printStackTrace();
			SharkUtil.error(message, "Something went wrong while trying to generate the image!");
		}
	}
	private int toInt(double val) throws ArithmeticException {
		return Math.toIntExact(Math.round(val));
	}

}
