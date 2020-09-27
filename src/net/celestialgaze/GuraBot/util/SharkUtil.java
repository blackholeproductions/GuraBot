package net.celestialgaze.GuraBot.util;

import java.awt.Color;
import java.util.Random;

import net.celestialgaze.GuraBot.GuraBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class SharkUtil {
	public static final int DEFAULT_RANDOM_SEED = 403126880;
	public static void error(Message message, String error) {
		error(message.getChannel(), error);
	}
	public static void error(MessageChannel channel, String error) {
		String[] failures = {"uh oh...", "is this a land thing that i'm not aware of?", "0% hydrodynamic"};
		channel.sendMessage(new EmbedBuilder()
				.setColor(new Color(242, 80, 80))
				.setTitle(failures[new Random().nextInt(failures.length)])
				.setDescription(error)
				.build()).queue();
	}
	public static void info(Message message, String info) {
		info(message.getChannel(), info);
	}
	public static void info(MessageChannel channel, String info) {
		channel.sendMessage(new EmbedBuilder()
				.setColor(GuraBot.DEFAULT_COLOR)
				.setDescription(info)
				.build()).queue();
	}
	public static void success(Message message, String success) {
		String[] exclamations = {"yay!", "woohoo", "i like salman"};
		message.getChannel().sendMessage(new EmbedBuilder()
				.setColor(new Color(152, 251, 152))
				.setTitle(exclamations[new Random().nextInt(exclamations.length)])
				.setDescription(success)
				.build()).queue();	
	}
	public static String toString(String[] array, String split) {
		String str = "";
		int i = 0;
		for (String s : array) {
			str += s + (i < array.length - 1 ? split : "");
			i++;
		}
		return str.trim();
	}
	public static double randomSeeded(String input) {
		return randomSeeded(input, DEFAULT_RANDOM_SEED);
	}
	public static double randomSeeded(String input, long seed) {
		OpenSimplexNoise noise = new OpenSimplexNoise(seed);
		Random random = new Random();
		int x = 0, y = 0;
		for (int i = 0; i < input.length(); i++) {
			int value = Math.toIntExact(Math.round((noise.eval(input.charAt(i)*100, 10)+1)*50));
			x += value;
			y += value;
		}
		random.setSeed(Math.toIntExact(Math.round((noise.eval(x, y)*Integer.MAX_VALUE))));
		return random.nextDouble();
	}
	public static String formatDuration(long milliseconds) {
		long seconds = milliseconds / 1000;
	    String positive = String.format(
	        "%dd%02dh%02dm%02ds",
	        seconds / 86400,
	        (seconds % 86400) / 3600,
	        (seconds % 3600) / 60,
	        seconds % 60);
	    return seconds < 0 ? "-" + positive : positive;
	}
}
