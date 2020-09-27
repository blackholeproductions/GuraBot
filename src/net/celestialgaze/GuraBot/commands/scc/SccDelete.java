package net.celestialgaze.GuraBot.commands.scc;

import java.util.Map;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.json.JSON;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class SccDelete extends Subcommand {

	public SccDelete(Command parent) {
		super(new CommandOptions()
				.setName("delete")
				.setDescription("Removes a command")
				.setUsage("<usage>")
				.setPermission(Permission.MANAGE_SERVER)
				.verify(),
				parent);
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		if (args.length != 1) {
			SharkUtil.error(message, Integer.toString(args.length));
			SharkUtil.error(message, "You must provide the name of the command to delete.");
			return;
		}
		
		String title = args[0];
		if (modifiers.length > 0 && modifiers[0].equalsIgnoreCase("global")) {
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
			return;
		}
		
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Map<String, Map<String, String>> m = si.getProperty(ServerProperty.COMMANDS);
		if (m.containsKey(title)) {
			m.remove(title);
		} else {
			SharkUtil.info(message, "No such command **" + title + "** exists");
			return;
		}
		si.setProperty(ServerProperty.COMMANDS, m);
		
		Commands.updateGuildCommands(message.getGuild().getIdLong());
		SharkUtil.success(message, "Successfully removed the **" + title + "** command");
	}
	
}
