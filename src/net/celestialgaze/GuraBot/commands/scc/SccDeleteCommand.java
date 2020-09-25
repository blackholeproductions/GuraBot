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

public class SccDeleteCommand extends Subcommand {

	protected SccDeleteCommand(Command parent) {
		super("delete", "<name>", "Removes a command", parent);
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args) {
		if (args.length != 1) {
			SharkUtil.error(message, Integer.toString(args.length));
			SharkUtil.error(message, "You must provide the name of the command to delete.");
			return;
		}
		
		String title = args[0];
		String filename = GuraBot.DATA_FOLDER+"bot\\commands.json";
		JSONObject jo = JSON.readFile(filename);
		if (jo.containsKey(title)) {
			jo.remove(title);
		} else {
			SharkUtil.info(message, "No such command **" + title + "** exists");
			return;
		}
		
		JSON.writeToFile(jo, filename);
		Commands.init(); // Initialize again to update the list of commands 
		
		SharkUtil.success(message, "Successfully removed the **" + title + "** command");
	}
	
}
