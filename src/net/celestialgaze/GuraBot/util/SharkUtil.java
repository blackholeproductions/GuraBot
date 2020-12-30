package net.celestialgaze.GuraBot.util;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import net.dv8tion.jda.api.entities.User;

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
	public static void debug(String message) {
		info(GuraBot.jda.getGuildById("720792335088353301").getDefaultChannel(), message);
	}
	public static void sendOwner(Guild guild, String info) {
		guild.getOwner().getUser().openPrivateChannel().queue(channel -> {
			SharkUtil.info(channel, info);
		}, failure -> {});
	}
	public static String toString(String[] array, String split) {
		return toString(array, split, 0);
	}
	public static String toString(String[] array, String split, int start) {
		String str = "";
		for (int i = start; i < array.length; i++) {
			String s = array[i];
			str += s + (i < array.length - 1 ? split : "");
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
	 * 
	 * @param date 
	 * @return a human-readable date in a format like Thu, 28 Apr 2016 01:47:31 GMT
	 */
	public static String formatDate(OffsetDateTime date) {
		return date.format(DateTimeFormatter.RFC_1123_DATE_TIME);
	}
	
	/**
	 * @param message
	 * @param args
	 * @param start Where the user string starts (args[start] should net the first argument)
	 * @return The member in question, or null if none was found.
	 */
	public static Member getMember(Message message, String[] args, int start) {
		Guild guild = message.getGuild();
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
					member = guild.getMemberById(id);
					if (member != null) return member;
				}
			}
			// Try to get by name
			try {
				if (guild.getMembersByName(input, true).size() > 0) {
					member = guild.getMembersByName(input, true).get(0);
					if (member != null) return member;
				}
			} catch (Exception e3) {}
			try {
				// Try to get by user#discrim
				if (guild.getMemberByTag(input) != null) {
					member = guild.getMemberByTag(input);
					if (member != null) return member;
				}
			} catch (Exception e2) {}
		}
		return member;
	}
	/**
	 * @param args Text to interpret
	 * @param start argument to start at
	 * @return user or null if none found
	 */
	public static User getUser(String[] args, int start) {
		User user = null;
		String input = "";
		for (int i = start; i < args.length; i++) {
			input += args[i] + (i == args.length - 1 ? "" : " ");
		}
		// Try to get user by tag
		try {
			user = GuraBot.jda.getUserByTag(input);
			if (user != null) return user;
		} catch (Exception e) {}
		// Try to get user by mention
		if (input.startsWith("<@!") && input.endsWith(">")) {
			String id = input.split("<@!")[1].split(">")[0];
			try {
				user = GuraBot.jda.getUserById(id);
				if (user != null) return user;
			} catch(Exception e) {}
		}
		// Try to get user by name
		List<User> possibleUsers = GuraBot.jda.getUsersByName(input, true);
		if (possibleUsers.size() > 0) user = GuraBot.jda.getUsersByName(input, true).get(0);
		
		return user;
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
	/**
	 * @param mention Mention to get ID from
	 * @return the id of the mention, 0 if invalid
	 */
	public static long getIdFromMention(String mention) {
		if (mention.startsWith("<@")) {
			try {
				return Long.parseLong(mention.split("<@")[1].substring(1).split(">")[0]); // Covers user mentions and role mentions
			} catch (Exception e) {}
		} else if (mention.startsWith("<#")) {
			try {
				return Long.parseLong(mention.split("<#")[1].split(">")[0]); // Covers channel mentions
			} catch (Exception e) {}
		}
		return (long)0;
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
