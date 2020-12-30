package net.celestialgaze.GuraBot.commands.module.settings;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.SettingsEdit;
import net.celestialgaze.GuraBot.commands.classes.SettingsList;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class ModuleSettings extends Subcommand {

	public ModuleSettings(Command parent) {
		super(new CommandOptions("settings", "Manage settings for modules")
				.setUsage("list|edit \"module name\"")
				.setPermission(Permission.MANAGE_SERVER)
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String[] quotes = (SharkUtil.toString(args, " ")).split("\"");
		if (args.length >= 2 && quotes.length >= 2) {
			String command = args[0];
			String moduleName = quotes[1];
			ModuleType type = null;
			try {
				type = ModuleType.valueOf(moduleName.replaceAll("\\s", "_").toUpperCase());;
			} catch (Exception e) {
				SharkUtil.error(message, "That's not the name of a module.");
				return;
			}
			if (command.equalsIgnoreCase("edit")) {
				if (quotes.length >= 3) {
					String[] newArgs = quotes[2].strip().split(" ");
					SettingsEdit edit = new SettingsEdit(this);
					edit.setModule(CommandModule.map.get(type));
					edit.attempt(message, newArgs, modifiers);
				} else {
					SharkUtil.error(message, "Invalid number of arguments");
				}
			} else if (command.equalsIgnoreCase("list")) {
				SettingsList list = new SettingsList(this);
				list.setModule(CommandModule.map.get(ModuleType.valueOf(moduleName.replaceAll("\\s", "_").toUpperCase())));
				list.attempt(message, new String[0], modifiers);
			} else if (command.equalsIgnoreCase("list|edit")) {
				SharkUtil.error(message, "you dont write both you fucking idiot");
			} else {
				SharkUtil.error(message, "Invalid subcommand");
			}
		} else {
			SharkUtil.error(message, "The correct usage is `" + this.getUsage() + "`");
		}
	}

}