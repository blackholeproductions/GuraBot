package net.celestialgaze.GuraBot.commands.modules.xp.roles;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class XpRolesRemove extends Subcommand {

	public XpRolesRemove(Command parent) {
		super(new CommandOptions()
				.setName("remove")
				.setDescription("Remove a role")
				.setUsage("<level>")
				.setUsablePrivate(false)
				.setPermission(Permission.MANAGE_SERVER)
				.verify(), parent);
		}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		SubDocBuilder sdb = new DocBuilder(xpDoc)
				.getSubDoc("settings")
				.getSubDoc("roles");
		int level = 0;
		if (args.length != 1) {
			SharkUtil.error(message, "Please specify a level");
			return;
		} else {
			try {
				level = Integer.parseInt(args[0]);
			} catch (Exception e) {
				SharkUtil.error(message, "Invalid level");
				return;
			}
		}
		if (sdb.has(Integer.toString(level))) {
			si.updateModuleDocument("xp", sdb.remove(Integer.toString(level)).build());
			SharkUtil.success(message, "Removed role at level " + level);
			return;
		}
		SharkUtil.info(message, "No role at level " + level);
	}

}
