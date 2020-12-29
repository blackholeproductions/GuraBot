package net.celestialgaze.GuraBot.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.ImageBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class TestCommand extends Command {

	public TestCommand() {
		super(new CommandOptions()
				.setName("test")
				.setDescription("hi")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		int width = 100,
			height = 100;
		if (args.length != 1) {
			SharkUtil.error(message, "Invalid arguments");
			return;
		}
		int color = 0;
		try {
			color = Integer.parseInt(args[0], 16);
		} catch (Exception e) {
			SharkUtil.error(message, "Invalid hex");
			return;
		}
		ImageBuilder ib = new ImageBuilder(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB))
				.setBackgroundColor(color);
		try {
			message.getChannel().sendFile(ib.build().toByteArray(), "color.png").embed(new EmbedBuilder()
					.setImage("attachment://color.png")
					.setColor(color)
					.setDescription(args[0])
					.build()).queue();
		} catch (IOException e) {
			e.printStackTrace();
			SharkUtil.error(message, "Error while generating image");
		}
	}

}
