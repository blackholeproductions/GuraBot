package net.celestialgaze.GuraBot.commands.modules.scc;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class CcDelete extends Subcommand {

	public CcDelete(Command parent) {
		super(new CommandOptions("delete", "Deletes a command")
				.setUsage("<name>")
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		DocBuilder cmdsDoc = new DocBuilder(si.getProperty(ServerProperty.COMMANDS, new Document()));
		String name = SharkUtil.toString(args, " ");
		
		si.setProperty(ServerProperty.COMMANDS, cmdsDoc.remove(name).build());
		Commands.updateGuildCommands(message.getGuild().getIdLong());
		SharkUtil.success(message, "Successfully deleted " + name + " command");
	}
}
