package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.SimpleCommand;
import net.celestialgaze.GuraBot.commands.scc.SimpleCmdCreator;
import net.celestialgaze.GuraBot.json.JSON;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;

public class Commands {
	public static String defaultPrefix = "a!";
	public static Map<String, HashMap<String, Command>> rootCommandsCategorized = new HashMap<String, HashMap<String, Command>>();
	public static Map<String, Command> rootCommands = new HashMap<String, Command>();
	public static Map<Long, HashMap<String, Command>> guildCommands = new HashMap<Long, HashMap<String, Command>>();
	private static List<Command> commands = new ArrayList<Command>();
	
	public static void addCommand(Command command) {
		commands.add(command);
		if (!rootCommandsCategorized.containsKey(command.getCategory()))
			rootCommandsCategorized.put(command.getCategory(), new HashMap<String, Command>());
		rootCommandsCategorized.get(command.getCategory()).put(command.getName(), command);
		System.out.println(command.getCategory());
		rootCommands.put(command.getName(), command);
	}
	@SuppressWarnings("unchecked")
	public static void init() {
		// Clear any existing commands
		commands.clear();
		rootCommands.clear();
		
		// Add root commands
		addCommand(new Ping());
		addCommand(new SetPrefix());
		addCommand(new Prefix());
		addCommand(new About());
		addCommand(new Noise());
		addCommand(new Ship());
		addCommand(new SimpleCmdCreator());
		addCommand(new Stats());
		addCommand(new Say());
		
		// Load commands from global commands json
		JSONObject jo = JSON.readFile(GuraBot.DATA_FOLDER+"bot\\commands.json");
		jo.forEach((key, value) -> {
			String title = (String)key;
			Map<String, String> m = (Map<String, String>)value;
			addCommand(new SimpleCommand(new CommandOptions()
					.setName(title)
					.setDescription(m.get("description"))
					.setCategory(m.get("category"))
					.verify(),
					m.get("response")));
		});
		
		// Help command last as it accesses the commands list
		addCommand(new MainHelp());
		
		// Populate guild commands
		for (int i = 0; i < GuraBot.jda.getGuilds().size(); i++) {
			System.out.printf("Updating guild commands... (%s/%s)\n", i+1, GuraBot.jda.getGuilds().size());
			updateGuildCommands(GuraBot.jda.getGuilds().get(i).getIdLong());
		}
	}
	
	public static void updateGuildCommands(long id) {
		guildCommands.put(id, new HashMap<String, Command>());
		Map<String, Command> guildMap = guildCommands.get(id);
		guildMap.clear();
		ServerInfo si = ServerInfo.getServerInfo(id);
		Map<String, HashMap<String, String>> m = si.getProperty(ServerProperty.COMMANDS, new HashMap<String, HashMap<String, String>>());
		m.forEach((commandName, properties) -> {
			guildMap.put(commandName, new SimpleCommand(new CommandOptions()
					.setName(commandName)
					.setDescription(properties.get("description"))
					.setCategory("Server Commands")
					.verify(),
					properties.get("response")));
		});
	}
	
}
