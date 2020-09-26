package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class CommandInterpreter {
	public static String getPrefix(Message message) {
		String content = message.getContentRaw();
		String[] args = content.split(GuraBot.REGEX_WHITESPACE);
		String rootCommand = args[0].toLowerCase();
		
		if (message.getChannelType().equals(ChannelType.PRIVATE)) return Commands.defaultPrefix;
		if (rootCommand.startsWith(Commands.defaultPrefix) && 
				(rootCommand.substring(Commands.defaultPrefix.length()).equalsIgnoreCase("setprefix") ||
				rootCommand.substring(Commands.defaultPrefix.length()).equalsIgnoreCase("prefix"))) {
			return Commands.defaultPrefix;
		}
		if (rootCommand.startsWith(ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix())) {
			return ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix();
		}
		return Commands.defaultPrefix;
	}
	public static boolean read(Message message) {
		String content = message.getContentRaw();
		String[] args = content.split(GuraBot.REGEX_WHITESPACE);
		String rootCommand = args[0].toLowerCase();
		// Check if leading argument is a valid command with the server prefix, with a prefix exception for the setprefix/prefix command
		if (rootCommand.startsWith(Commands.defaultPrefix) && 
				(rootCommand.substring(Commands.defaultPrefix.length()).equalsIgnoreCase("setprefix") ||
				rootCommand.substring(Commands.defaultPrefix.length()).equalsIgnoreCase("prefix"))) {
			return true;
		}
		if ((rootCommand.startsWith(Commands.defaultPrefix) && message.getChannelType().equals(ChannelType.PRIVATE)) ||
				(message.getChannelType().equals(ChannelType.TEXT) && 
				rootCommand.startsWith(ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix()))) {
			for (Command command : Commands.rootCommands.values()) {
				if (rootCommand.endsWith(command.getName())) return true;
			}
			return false;
		}
		return false;
	}
	
	public static boolean readExecute(Message message) {
		if (read(message)) {
			execute(message);
			return true;
		}
		return false;
	}
	
	public static void execute(Message message) {
		String content = message.getContentRaw();
		String[] args = content.split(GuraBot.REGEX_WHITESPACE);
		// Remove redundant spaces
		List<String> argsList = Arrays.asList(args);
		argsList.removeIf(item -> item.isEmpty() || item == null);
		argsList.toArray(args);
		Command command = Commands.rootCommands.get(args[0].toLowerCase().substring(getPrefix(message).length()));
		// Loop through each argument. We will check where commands end and arguments begin, as well as which command to run.
		for (int i = 0; i < args.length; i++) {
			boolean end = false;
			if (command == null || command.getSubcommands().size() == 0) end = true; // Skip the following if there are no subcommands
			// Check if the next argument is a subcommand of the current command
			for (int j = 0; j < command.getSubcommands().size(); j++) {
				if (end) break;
				if (i+1 >= args.length || args[i+1].equalsIgnoreCase(command.getSubcommands().get(j).getName())) {
					if (!(i+1 >= args.length)) {
						command = command.getSubcommands().get(j);
						i++;
					}
					end = true;
					break;
				} else if (j+1 >= command.getSubcommands().size()) { 
					if (args[i+1].equalsIgnoreCase(command.getSubcommands().get(j).getName())) 
						command = command.getSubcommands().get(j);
					j = 0;
					i++;
				}
			}
			if (end) {
				List<String> modifiersList = new ArrayList<String>();
				for (int j = args.length-1; j >= 0; j--) {
					if (args[j].startsWith("--")) {
						modifiersList.add(args[j]);
					}
				}
				String[] modifiers = new String[modifiersList.size()];
				modifiersList.toArray(modifiers);
				String[] newArgs = new String[args.length-(i+1)-modifiers.length];
				int j = 0;
				int k = 0;
				for (String arg : args) {
					if (k > i && j+i < newArgs.length) {
						newArgs[j] = arg;
						j++;
					}
					k++;
				}
				command.attempt(message, newArgs, modifiers);
				break;
			}
		}
	}
}
