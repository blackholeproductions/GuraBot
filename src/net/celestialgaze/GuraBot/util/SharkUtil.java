package net.celestialgaze.GuraBot.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.IPageCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

public class SharkUtil {
	public static final int DEFAULT_RANDOM_SEED = 403126880;
	public static final MessageEmbed WAITING_EMBED = new EmbedBuilder()
			.setTitle("Waiting")
			.setColor(new Color(127, 127, 127)).build();
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
	public static void sendOwner(Guild guild, String info) {
		guild.getOwner().getUser().openPrivateChannel().queue(channel -> {
			SharkUtil.info(channel, info);
		}, failure -> {});
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
	/**
	 * 
	 * @param milliseconds 
	 * @return a human-readable duration in a format like 0d 00h 00m 00.000s
	 */
	public static String formatDuration(long milliseconds) {
		long totalSeconds = milliseconds / 1000;
		long d = totalSeconds / 86400;
		int h = (int) ((totalSeconds % 86400) / 3600);
		int m = (int) ((totalSeconds % 3600) / 60);
		int s = (int) (totalSeconds % 60);
		int ms = (int) (milliseconds % 1000);
		
	    return (d > 0 ? d + "d " : "") + (h > 0 ? h + "h " : "") + (m > 0 ? m + "m " : "") + 
	    		((d == 0 && m == 0 && m == 0) || s > 0 || ms > 0 ? s + (ms > 0 ? "." + String.format("%03d", ms) : "") + "s": "");
	}
	
	/**
	 * @param guild Guild to search
	 * @param args
	 * @param start Where the user string starts (args[start] should net the first argument)
	 * @return The member in question, or null if none was found.
	 */
	public static Member getMember(Guild guild, String[] args, int start) {
		Member member = null;
		String input = "";
		for (int i = start; i < args.length; i++) {
			input += args[i] + (i == args.length - 1 ? "" : " ");
		}
		// Try to get by ID
		try {
			member = guild.getMemberById(input);
		} catch (Exception e) {}
		if (member == null) {
			// Try to get by mention
			if (input.startsWith("<@!") && input.endsWith(">")) {
				String id = input.split("<@!")[1].split(">")[0];
				if (guild.getMemberById(id) != null) {
					return guild.getMemberById(id);
				}
			}
			// Try to get by name
			try {
				if (guild.getMembersByName(input, true).size() > 0) {
					return guild.getMembersByName(input, true).get(0);
				}
			} catch (Exception e3) {}
			try {
				// Try to get by user#discrim
				if (guild.getMemberByTag(input) != null) {
					return guild.getMemberByTag(input);
				}
			} catch (Exception e2) {}
		}
		return member;
	}
	
	/**
	 * @param str String to parse
	 * @return true if can parse to int, false otherwise
	 */
	public static boolean canParseInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * @param str String to parse
	 * @return the parsed integer, returns 0 if invalid
	 */
	public static int parseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * @param str String to parse
	 * @return true if can parse to double, false otherwise
	 */
	public static boolean canParseDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * @param str String to parse
	 * @return the parsed double, returns 0 if invalid
	 */
	public static double parseDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	public static Role getRole(Message message, String[] args, int start) {
		Guild guild = message.getGuild();
		Role role = null;
		String input = "";
		for (int i = start; i < args.length; i++) {
			input += args[i] + " ";
		}
		input = input.strip();
		// Try to get by ID
		try {
			role = guild.getRoleById(input);
		} catch (Exception e) {
			// Try to get by mention
			if (input.startsWith("<@&") && input.endsWith(">")) {
				String id = input.split("<@&")[1].split(">")[0];
				if (guild.getRoleById(id) != null) {
					return guild.getRoleById(id);
				}
			}
			try {
				// Try to get by name
				if (guild.getRolesByName(input, true).size() > 0) {
					role = guild.getRolesByName(input, true).get(0);
				}
			} catch (Exception e2) {}
		}
		return role;
	}
	
	public static void sendHelpMenu(Message message, IPageCommand helpCmd) {
		sendHelpMenu(message, helpCmd, null);
	}
	public static void sendHelpMenu(Message message, IPageCommand helpCmd, ArrayList<Command> extra) {
		message.getChannel().sendMessage(new EmbedBuilder().setTitle("Waiting...").build()).queue(response -> {
			PageMessage pm = new PageMessage(response, message.getAuthor().getIdLong(), new ArgRunnable<Integer>() {

				@Override
				public void run() {
					response.editMessage(helpCmd.createEmbed(message, response, getArg(), extra).build()).queue();
				}
				
			});
			pm.update();
		});
	}

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
    

}
