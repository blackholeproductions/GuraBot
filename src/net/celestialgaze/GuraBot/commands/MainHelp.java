package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class MainHelp extends HelpCommand {

	public MainHelp() {
		super(new CommandOptions()
				.setName("help")
				.setDescription("Shows a list of available commands and any information about them.")
				.setCategory("Bot Info")
				.verify(), GuraBot.jda.getSelfUser().getName());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void commandInit() {
		for (HashMap<String, Command> category : Commands.rootCommandsCategorized.values()) {
			for(Command command : category.values()) {
				commands.put(command.getName(), command);
			}
		}
	}
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		Map<String, Command> guildCommands = (message.getChannelType().equals(ChannelType.TEXT) ? 
				Commands.guildCommands.get(message.getGuild().getIdLong()) :
				new HashMap<String, Command>());
		
		// If this is a guild channel
		if (message.getChannelType().equals(ChannelType.TEXT)) {
			// Add any enabled module commands
			for (CommandModule module : Commands.modules.values()) {
				if (module.isEnabled(message.getGuild().getIdLong())) {
					for (Command command : module.getCommandsList()) {
						commands.put(command.getName(), command);
					}
				}
			}
		}
		SharkUtil.sendHelpMenu(message, this, new ArrayList<Command>(guildCommands.values()));
	}
}
