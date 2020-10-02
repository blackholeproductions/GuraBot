package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bson.Document;
import com.mongodb.client.model.Filters;

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
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
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
							
							// Return if channel has disabled xp
							SubDocBuilder sdb = new DocBuilder(si.getModuleDocument("xp")).getSubDoc("settings").getSubDoc("toggle");
							String type = sdb.get("mode", "blacklist");
							List<String> greylist = sdb.get("list", new ArrayList<String>());
							if (greylist.size() > 0) {
								if (greylist.contains(Long.toString(event.getChannel().getIdLong()))) {
									if (type.equalsIgnoreCase("blacklist")) return;
								} else {
									if (type.equalsIgnoreCase("whitelist")) return;
								}
							}
							
							// Add XP and cooldown
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
		// Load commands from global commands document
		Document cmdsDoc = GuraBot.bot.find(Filters.eq("name", "commands")).first();
		cmdsDoc.forEach((key, value) -> {
			if (value instanceof Document) {
				String title = (String)key;
				Document cmdDoc = (Document)value;
				addCommand(new SimpleCommand(new CommandOptions()
						.setName(title)
						.setDescription(cmdDoc.getString("description"))
						.setCategory(cmdDoc.getString("category"))
						.verify(),
						cmdDoc.getString("response")));
			}
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
		Document doc = si.getProperty(ServerProperty.COMMANDS, new Document());
		doc.forEach((commandName, properties) -> {
			Document propertiesDoc = (Document) properties;
			guildMap.put(commandName, new SimpleCommand(new CommandOptions()
					.setName(commandName)
					.setDescription((String) propertiesDoc.get("description"))
					.setCategory("Server Commands")
					.verify(),
					(String) propertiesDoc.get("response")));
		});
	}
	
}
