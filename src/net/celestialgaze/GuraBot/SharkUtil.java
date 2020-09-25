package net.celestialgaze.GuraBot;

import java.awt.Color;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class SharkUtil {
	public static void error(Message message, String error) {
		String[] failures = {"uh oh...", "is this a land thing that i'm not aware of?", "0% hydrodynamic"};
		message.getChannel().sendMessage(new EmbedBuilder()
				.setColor(new Color(242, 80, 80))
				.setTitle(failures[new Random().nextInt(failures.length-1)])
				.setDescription(error)
				.build()).queue();
	}
	public static void info(Message message, String info) {
		message.getChannel().sendMessage(new EmbedBuilder()
				.setColor(GuraBot.DEFAULT_COLOR)
				.setDescription(info)
				.build()).queue();
		
	}
	public static void success(Message message, String success) {
		String[] exclamations = {"yay!", "woohoo"};
		message.getChannel().sendMessage(new EmbedBuilder()
				.setColor(new Color(152, 251, 152))
				.setTitle(exclamations[new Random().nextInt(exclamations.length-1)])
				.setDescription(success)
				.build()).queue();	
	}
}
