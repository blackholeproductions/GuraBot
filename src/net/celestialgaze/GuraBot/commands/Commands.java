package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import com.mongodb.client.model.Filters;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.SimpleCommand;
import net.celestialgaze.GuraBot.commands.classes.SimpleCommandModule;
import net.celestialgaze.GuraBot.commands.module.ModuleCmd;
import net.celestialgaze.GuraBot.commands.modules.counting.Counting;
import net.celestialgaze.GuraBot.commands.modules.counting.CountingModule;
import net.celestialgaze.GuraBot.commands.modules.counting.CountingSetChannel;
import net.celestialgaze.GuraBot.commands.modules.moderation.Ban;
import net.celestialgaze.GuraBot.commands.modules.moderation.Kick;
import net.celestialgaze.GuraBot.commands.modules.moderation.ModerationModule;
import net.celestialgaze.GuraBot.commands.modules.moderation.Purge;
import net.celestialgaze.GuraBot.commands.modules.scc.CommandCreator;
import net.celestialgaze.GuraBot.commands.modules.typing.TypeCmd;
import net.celestialgaze.GuraBot.commands.modules.typing.TypingModule;
import net.celestialgaze.GuraBot.commands.modules.xp.Xp;
import net.celestialgaze.GuraBot.commands.modules.xp.XpModule;
import net.celestialgaze.GuraBot.commands.modules.xp.Leaderboard;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;

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
		addCommand(new UserInfo());
		addCommand(new Roles());
		addCommand(new RoleInfo());
		addCommand(new Benchmark());
		addCommand(new SnailRace());
		addCommand(new AI());
		addCommand(new TestCommand());
		addCommand(new SimpleCommand(new CommandOptions("github", "Gets a link to the bot's github").verify(), "https://github.com/celestialgaze3/GuraBot"));
		
		// Modules
		addModule(new SimpleCommandModule(ModuleType.CUSTOM_COMMANDS,
			new CommandCreator()
		));
		
		addModule(new XpModule(
			new Xp(),
			new Leaderboard()
		));
		
		addModule(new TypingModule(
			new TypeCmd()
		));
		
		addModule(new ModerationModule(
			new Kick(),
			new Ban(),
			new Purge()
		));
		
		addModule(new CountingModule(
			new Counting()
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
		if (si.getProperty(ServerProperty.COMMANDS, new Document()) instanceof Document) {
			DocBuilder doc = new DocBuilder(si.getProperty(ServerProperty.COMMANDS, new Document()));
			doc.build().forEach((commandName, properties) -> {
				Document propertiesDoc = doc.getSubDoc(commandName).buildThis();
				guildMap.put(commandName, new SimpleCommand(new CommandOptions()
						.setName(commandName)
						.setDescription((String) propertiesDoc.get("description"))
						.setCategory("Server Commands")
						.verify(),
						(String) propertiesDoc.getOrDefault("response", "Error retrieving simple command text")));
			});
		}
		
	}
	
}
