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
import net.dv8tion.jda.api.entities.Role;

public class XpRolesAdd extends Subcommand {

	public XpRolesAdd(Command parent) {
		super(new CommandOptions()
				.setName("add")
				.setDescription("Adds role at level")
				.setUsage("<level> <role>")
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
		if (args.length < 2) {
			SharkUtil.error(message, this.usage);
			return;
		} else {
			try {
				level = Integer.parseInt(args[0]);
			} catch (Exception e) {
				SharkUtil.error(message, "Invalid level");
				return;
			}
		}
		Role role = SharkUtil.getRole(message, args, 1);
		if (role != null) {
			si.updateModuleDocument("xp", sdb.put(args[0], role.getId()).build());
			SharkUtil.success(message, "Will add **" + role.getName() + "** when a user reaches level " + Integer.toString(level));
			return;
		}
		SharkUtil.error(message, "Invalid role");
	}

}
