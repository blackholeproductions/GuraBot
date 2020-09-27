package net.celestialgaze.GuraBot.commands.module;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class ModuleDisable extends Subcommand {

	public ModuleDisable(Command parent) {
		super(new CommandOptions()
				.setName("disable")
				.setDescription("Disables a module in your server")
				.setUsablePrivate(false)
				.setPermission(Permission.MANAGE_SERVER)
				.setUsage("\"module name\"")
				.verify(),
				parent);
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String argsString = SharkUtil.toString(args, " ");
		String[] quoteSplit = argsString.split("\"");
		if (quoteSplit.length != 2) {
			SharkUtil.error(message, "You have entered an invalid number of arguments");
			return;
		}
		CommandModule module = null;
		try {
			module = Commands.modules.get(ModuleType.valueOf(quoteSplit[1].replaceAll("\\s", "_").toUpperCase()));
		} catch (Exception e) {
			SharkUtil.error(message, "That is not a valid module");
			return;
		}
		module.setDisabled(message.getGuild().getIdLong());
		SharkUtil.success(message, "Disabled **" + module.getName() + "** module!");
	}

}
