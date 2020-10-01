package net.celestialgaze.GuraBot.commands.modules.scc;

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
			Document document = GuraBot.bot.find(Filters.eq("name", "commands")).first();
			
			if (document.containsKey(title)) {
				document.remove(title);
			} else {
				SharkUtil.error(message, "No such command **" + title + "** exists.");
			}
			GuraBot.bot.replaceOne(Filters.eq("name", "commands"), document);
			
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
