package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;

public class Commands {
	public static String defaultPrefix = "a!";
	public static Map<String, Command> rootCommands = new HashMap<String, Command>();
	private static List<Command> commands = new ArrayList<Command>();
	
	public static void addCommand(Command command) {
		commands.add(command);
		rootCommands.put(command.getName(), command);
	}
	public static void init() {
		PingCommand ping = new PingCommand(
				"ping",
				"",
				"Pings the bot and gives you the latency.");
		SetPrefixCommand setprefix = new SetPrefixCommand(
				"setprefix",
				"<prefix>",
				"Sets the prefix of your server");
		PrefixCommand prefix = new PrefixCommand(
				"prefix",
				"",
				"Gets the current prefix of the server. Usable with the default prefix universally.");
		HelpCommand help = new HelpCommand(
				"help",
				"",
				"Shows a list of available commands and any information about them.");
		AboutCommand about = new AboutCommand(
				"about",
				"",
				"Information about me, " + GuraBot.jda.getSelfUser().getName()+ "~");
		NoiseCommand noise = new NoiseCommand(
				"noise",
				"<x> <y> <z>",
				"Gets OpenSimplex noise value at <x>, <y>, <z>.");
		ShipCommand ship = new ShipCommand(
				"ship",
				"<thing1> <thing2>",
				"Tells you how much they're destined for true love");
	}
	
}
