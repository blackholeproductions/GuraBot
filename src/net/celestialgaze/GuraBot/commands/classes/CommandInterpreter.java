package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.List;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class CommandInterpreter {
	public static String getPrefix(Message message) {
		String content = message.getContentRaw();
		String[] args = content.split(GuraBot.REGEX_WHITESPACE);
		String rootCommand = args[0].toLowerCase();
		
		if (message.getChannelType().equals(ChannelType.PRIVATE) && rootCommand.startsWith(Commands.defaultPrefix)) {
			return Commands.defaultPrefix;
		}
		if (message.getChannelType().equals(ChannelType.TEXT)) {
			String prefix = ServerInfo.getServerInfo(message.getGuild().getIdLong()).getProperty(ServerProperty.PREFIX, Commands.defaultPrefix);
			if (rootCommand.startsWith(Commands.defaultPrefix) && 
					(rootCommand.substring(Commands.defaultPrefix.length()).equalsIgnoreCase("setprefix") ||
					rootCommand.substring(Commands.defaultPrefix.length()).equalsIgnoreCase("prefix"))) {
				return Commands.defaultPrefix;
			}
			if (rootCommand.startsWith(prefix)) {
				return prefix;
			}	
		}
		return null;
	}
	public static boolean read(Message message) {
		String content = message.getContentRaw();
		String[] args = content.split(GuraBot.REGEX_WHITESPACE);
		String rootCommand = args[0].toLowerCase();
		String commandName = (getPrefix(message) != null ? rootCommand.substring(getPrefix(message).length()) : "");
		
		// Check if leading argument is a valid command with the server prefix, with a prefix exception for the setprefix/prefix command
		if (commandName == "") return false; 
		if (commandName.equalsIgnoreCase("setprefix") || commandName.equalsIgnoreCase("prefix")) {
			return true;
		}
		// if in a private channel and using the default prefix
		if (message.getChannelType().equals(ChannelType.PRIVATE) && getPrefix(message).equalsIgnoreCase(Commands.defaultPrefix)) {
			// if valid command
			if (Commands.rootCommands.containsKey(commandName)) {
				return true;
			}
		}
		// if in a text channel and using the server prefix
		if (message.getChannelType().equals(ChannelType.TEXT) && 
				getPrefix(message).equalsIgnoreCase(ServerInfo.getServerInfo(message.getGuild().getIdLong()).getProperty(ServerProperty.PREFIX, Commands.defaultPrefix))) {
			// if valid command or guild command or enabled module command
			if (Commands.rootCommands.containsKey(commandName) ||
					Commands.guildCommands.get(message.getGuild().getIdLong()).containsKey(commandName) ||
					(Commands.moduleCommands.containsKey(commandName) && Commands.moduleCommands.get(commandName).canRun(message))) {
				return true;
			}
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
		String prefix = getPrefix(message);
		String rootCommandName = args[0].toLowerCase().substring(prefix.length());
		
		int indexCommandsEnd = 0; // The index where commands end. indexCommandsEnd+1 would be the index of the first argument.
		Command commandToRun = null;
		// First check if this command is a guild command.
		if (message.getChannelType().equals(ChannelType.TEXT) && 
				Commands.guildCommands.get(message.getGuild().getIdLong()).containsKey(rootCommandName)) {
			
			Commands.guildCommands.get(message.getGuild().getIdLong()).get(rootCommandName)
				.run(message, new String[0], new String[0]); // We can run this right away as there are no args and no modifiers that could affect anything
			return;
		} else { // Not a guild command
			// Check if the command is from an enabled module
			if (Commands.moduleCommands.containsKey(rootCommandName) && Commands.moduleCommands.get(rootCommandName).canRun(message)) {
				commandToRun = Commands.moduleCommands.get(rootCommandName);
			} else { // Not an enabled module command
				// Set the command to run to the first argument. We can assume this is valid as it is checked in read()
				commandToRun = Commands.rootCommands.get(rootCommandName);
			}
			if (commandToRun != null)
			// Loop through each argument to find indexCommandsEnd, and the command to run
			for (int i = 0; i < args.length; i++) {
				// Look ahead one argument and check if it is a valid subcommand of the current command.
				if (i+1 >= args.length) break; // If there is no next argument, exit the loop now
				if (commandToRun.getSubcommands().containsKey(args[i+1])) { // Is subcommand
					commandToRun = commandToRun.getSubcommands().get(args[i+1]); // Set command to run to subcommand
				} else { // Next argument is not a subcommand
					indexCommandsEnd = i; // Set the index
					break; // Exit loop
				}
			}
		}
		if (commandToRun != null) {
			// Strip args of all commands
			String[] argsCopy = args;
			args = new String[args.length-(indexCommandsEnd+1)];
			for (int i = 0; i < args.length; i++) {
				args[i] = argsCopy[i+(indexCommandsEnd+1)];
			}
			
			// Extract all modifiers from args into modifiers array
			List<String> modifiersList = new ArrayList<String>();
			for (int i = args.length-1; i >= 0; i--) {
				if (args[i].startsWith("--")) {
					modifiersList.add(args[i].substring(2));
				}
			}
			
			// Convert modifiers list to array
			String[] modifiers = new String[modifiersList.size()];
			modifiersList.toArray(modifiers);
			
			// Remove from args
			argsCopy = args;
			args = new String[args.length-modifiersList.size()];
			for (int i = 0; i < args.length; i++) {
				args[i] = argsCopy[i];
			}
			
			// Run command
			commandToRun.attempt(message, args, modifiers);
		} else {
			SharkUtil.error(message, "Something has gone wrong in my command interpreter, as the command to run " + 
					"was not identified. Either cel's bad at coding or the command you tried to run has been " +
					"set up improperly.");
		}
	}
}
