package net.celestialgaze.GuraBot.commands.scc;

import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.json.JSON;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class SccCreateCommand extends Subcommand {

	protected SccCreateCommand(Command parent) {
		super("create", //title
			  "\"title\" \"description\" \"response\"", //usage
			  "Creates simple (text-only responses) commands", //description
			  parent);
	}

	@Override
	public void init() {}

	@SuppressWarnings("unchecked")
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String argsString = SharkUtil.toString(args, " ");
		if (argsString.split("\"").length != 6) {
			SharkUtil.error(message, "You have entered an invalid number of arguments");
			return;
		}
		String title = argsString.split("\"")[1].replaceAll("\\s", "");
		title = title.replaceAll("\\s", ""); // remove all whitespace
		String description = argsString.split("\"")[3];
		String response = argsString.split("\"")[5];
		
		if (title.length() >= 100) {
			SharkUtil.error(message, "The title cannot be longer than 100 characters. Current: " + title.length());
			return;
		} else if (description.length() >= 512) {
			SharkUtil.error(message, "The description cannot be longer than 512 characters. Current: " + description.length());
			return;
		}
		
		String filename = GuraBot.DATA_FOLDER+"bot\\commands.json";
		JSONObject jo = JSON.readFile(filename);
		
		Map<String, String> m = new LinkedHashMap<String, String>(2);
		m.put("description", description);
		m.put("response", response);
		jo.put(title, m);
		
		JSON.writeToFile(jo, filename);
		Commands.init(); // Initialize again to include the newly-created command
		
		SharkUtil.success(message, "Successfully created **" + title + "** command");
	}

}
