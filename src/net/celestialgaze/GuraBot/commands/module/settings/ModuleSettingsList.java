package net.celestialgaze.GuraBot.commands.module.settings;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class ModuleSettingsList extends Subcommand {

	public ModuleSettingsList(Command parent) {
		super(new CommandOptions()
				.setName("list")
				.setDescription("Gets a list of all settings for the module")
				.setUsage("\"Module Name\"")
				.setUsablePrivate(false)
				.setNeedAdmin(true)
				.verify(), parent);
	}

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
		String toSend = "";
		Document settings = module.getSettings(message.getGuild().getIdLong()).buildThis();
		Iterator<Entry<String, Object>> iterator = settings.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			if (!(entry.getValue() instanceof Document)) {
				toSend += entry.getKey() + ": " + entry.getValue() + "\n";
			}
			
		}
		message.getChannel().sendMessage(toSend).queue();
	}

}
