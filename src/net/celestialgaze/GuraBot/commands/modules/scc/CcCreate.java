package net.celestialgaze.GuraBot.commands.modules.scc;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class CcCreate extends Subcommand {

	public CcCreate(Command parent) {
		super(new CommandOptions("create", "Creates a command")
				.setUsage("\"name\" \"description\" \"response\"")
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		DocBuilder cmdsDoc = new DocBuilder(si.getProperty(ServerProperty.COMMANDS, new Document()));
		String[] quotes = SharkUtil.toString(args, " ").split("\"");
		if (quotes.length == 6) {
			String name = quotes[1];
			String description = quotes[3];
			String response = quotes[5];
			
			if (Commands.rootCommands.containsKey(name)) {
				SharkUtil.error(message, "A command already exists with that name.");
				return;
			}
			
			SubDocBuilder cmdDoc = cmdsDoc.getSubDoc(name);
			cmdDoc.put("description", description);
			cmdDoc.put("response", response);
			
			si.setProperty(ServerProperty.COMMANDS, cmdDoc.build());
			Commands.updateGuildCommands(message.getGuild().getIdLong());
			SharkUtil.success(message, "Successfully created " + name + " command");
		} else {
			SharkUtil.error(message, "Invalid number of arguments");
		}
	}

}
