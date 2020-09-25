package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.JSON;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.SimpleCommand;
import net.celestialgaze.GuraBot.commands.scc.SimpleCmdCreatorCommand;

public class Commands {
	public static String defaultPrefix = "a!";
	public static Map<String, Command> rootCommands = new HashMap<String, Command>();
	private static List<Command> commands = new ArrayList<Command>();
	
	public static void addCommand(Command command) {
		commands.add(command);
		rootCommands.put(command.getName(), command);
	}
	@SuppressWarnings("unchecked")
	public static void init() {
		commands.clear();
		rootCommands.clear();
		addCommand(new PingCommand(
				"ping",
				"",
				"Pings the bot and gives you the latency."));
		addCommand(new SetPrefixCommand(
				"setprefix",
				"<prefix>",
				"Sets the prefix of your server"));
		addCommand(new PrefixCommand(
				"prefix",
				"",
				"Gets the current prefix of the server. Usable with the default prefix universally."));
		addCommand(new AboutCommand(
				"about",
				"",
				"Information about me, " + GuraBot.jda.getSelfUser().getName()+ "~"));
		addCommand(new NoiseCommand(
				"noise",
				"<x> <y> <z>",
				"Gets OpenSimplex noise value at <x>, <y>, <z>."));
		addCommand(new ShipCommand(
				"ship",
				"<thing1> <thing2> Optional: (<seed> | --brute-force | --brute-force-lowest)",
				"Tells you how much they're destined for true love (or you can rig it)"));
		addCommand(new SimpleCmdCreatorCommand(
				"scc",
				"",
				"Manages simple (text-only responses) commands"));
		// Load commands from json
		JSONObject jo = JSON.readFile(GuraBot.DATA_FOLDER+"bot\\commands.json");
		jo.forEach((key, value) -> {
			String title = (String)key;
			Map<String, String> m = (Map<String, String>)value;
			addCommand(new SimpleCommand(title, m.get("description"), m.get("response")));
		});
		// Help command last as it accesses the commands list
		addCommand(new MainHelpCommand(
				"help",
				"",
				"Shows a list of available commands and any information about them."));
		
		
	}
	
}
