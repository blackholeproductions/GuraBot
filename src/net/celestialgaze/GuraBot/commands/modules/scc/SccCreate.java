package net.celestialgaze.GuraBot.commands.modules.scc;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.model.Filters;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class SccCreate extends Subcommand {

	public SccCreate(Command parent) {
		super(new CommandOptions()
				.setName("create")
				.setDescription("Creates simple (text-only responses) commands")
				.setUsage("\"title\" \"description\" \"response\"")
				.setPermission(Permission.MANAGE_SERVER)
				.verify(),
				parent);
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String argsString = message.getContentRaw();
		String[] quoteSplit = argsString.split("\"");
		if (quoteSplit.length < 6 || quoteSplit.length > 9) {
			SharkUtil.error(message, "You have entered an invalid number of arguments");
			return;
		}
		
		String title = quoteSplit[1].replaceAll("\\s", "").toLowerCase();
		title = title.replaceAll(GuraBot.REGEX_WHITESPACE, ""); // remove all whitespace
		String description = quoteSplit[3];
		String response = quoteSplit[5];
		String category = Command.DEFAULT_CATEGORY;
		if (title.length() >= 100) {
			SharkUtil.error(message, "The title cannot be longer than 100 characters. Current: " + title.length());
			return;
		} else if (description.length() >= 512) {
			SharkUtil.error(message, "The description cannot be longer than 512 characters. Current: " + description.length());
			return;
		}
		// Prevent command overriding
		if (Commands.rootCommands.containsKey(title) || Commands.moduleCommands.containsKey(title)) {
			SharkUtil.error(message, "Looks like there's already a global command with the name **" + title + "**. Please choose another");
			return;
		}
		
		Map<String, String> command = new LinkedHashMap<String, String>(3);
		command.put("description", description);
		command.put("response", response);
		
		if (modifiers.length > 0 && modifiers[0].equalsIgnoreCase("global")) {
			Document document = GuraBot.bot.find(Filters.eq("name", "commands")).first();
			
			if (quoteSplit.length == 9) category = quoteSplit[7];
			command.put("category", category);
			
			document.put(title, command);
			GuraBot.bot.replaceOne(Filters.eq("name", "commands"), document);
			
			Commands.init(); // Initialize again to include the newly-created command
			SharkUtil.success(message, "Successfully created **" + title + "** command");
			return;
		}
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Map<String, Map<String, String>> m = si.getProperty(ServerProperty.COMMANDS, new LinkedHashMap<String, Map<String, String>>());
		m.put(title, command);
		si.setProperty(ServerProperty.COMMANDS, m);
		
		Commands.updateGuildCommands(message.getGuild().getIdLong());
		SharkUtil.success(message, "Successfully created **" + title + "** command");
	}

}
