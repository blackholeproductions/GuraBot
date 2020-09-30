package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.SimpleCommand;
import net.celestialgaze.GuraBot.commands.module.ModuleCmd;
import net.celestialgaze.GuraBot.commands.modules.scc.SimpleCmdCreator;
import net.celestialgaze.GuraBot.commands.modules.xp.Xp;
import net.celestialgaze.GuraBot.commands.modules.xp.XpLeaderboard;
import net.celestialgaze.GuraBot.json.JSON;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.DelayedRunnable;
import net.celestialgaze.GuraBot.util.RunnableListener;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Commands {
	public static String defaultPrefix = "a!";
	public static Map<String, HashMap<String, Command>> rootCommandsCategorized = new HashMap<String, HashMap<String, Command>>();
	public static Map<String, Command> rootCommands = new HashMap<String, Command>();
	public static Map<Long, HashMap<String, Command>> guildCommands = new HashMap<Long, HashMap<String, Command>>();
	public static Map<ModuleType, CommandModule> modules = new HashMap<ModuleType, CommandModule>();
	public static Map<String, Command> moduleCommands = new HashMap<String, Command>();
	private static List<Command> commands = new ArrayList<Command>();
	
	public static void addCommand(Command command) {
		commands.add(command);
		if (!rootCommandsCategorized.containsKey(command.getCategory()))
			rootCommandsCategorized.put(command.getCategory(), new HashMap<String, Command>());
		rootCommandsCategorized.get(command.getCategory()).put(command.getName(), command);
		System.out.println(command.getCategory());
		rootCommands.put(command.getName(), command);
	}
	
	public static void addModule(CommandModule module) {
		modules.put(module.getType(), module);
		for (Command command : module.getCommandsList()) {
			moduleCommands.put(command.getName(), command);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void init() {
		// Clear any existing commands
		commands.clear();
		rootCommands.clear();
		
		// Add root commands
		addCommand(new Ping());
		addCommand(new Info());
		addCommand(new SetPrefix());
		addCommand(new Prefix());
		addCommand(new About());
		addCommand(new Noise());
		addCommand(new Ship());
		addCommand(new Stats());
		addCommand(new Say());
		addCommand(new Avatar());
		addCommand(new ModuleCmd());
		
		// Modules
		addModule(new CommandModule(ModuleType.CUSTOM_COMMANDS,
			new SimpleCmdCreator()
		));
		addModule(new CommandModule(ModuleType.XP,
			new RunnableListener() {
				List<Long> cooldowns = new ArrayList<Long>(); // Users that currently have a cooldown for XP
				@Override
				public void run() {
					if (currentEvent instanceof MessageReceivedEvent) {
						MessageReceivedEvent event = (MessageReceivedEvent) currentEvent;
						if (event.getChannelType().equals(ChannelType.TEXT) && 
								!CommandModule.isEnabled(ModuleType.XP, event.getGuild().getIdLong())) return; // If not enabled or not in guild, don't run 
						Long userId = event.getAuthor().getIdLong();
						if (!cooldowns.contains(userId)) { // If user isn't on cooldown
							ServerInfo si = ServerInfo.getServerInfo(event.getGuild().getIdLong());
							si.addXP(userId, 20+new Random().nextInt(5)); // Add xp
							cooldowns.add(userId);
							new DelayedRunnable(new Runnable() {

								@Override
								public void run() {
									cooldowns.remove(userId);
								}
								
							}).execute(System.currentTimeMillis()+(30*1000)); // Remove after 30 seconds
						}
					}
				}
			},
			new Xp(),
			new XpLeaderboard()
		));
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
