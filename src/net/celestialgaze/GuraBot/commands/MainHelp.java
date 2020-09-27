package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.dv8tion.jda.api.EmbedBuilder;
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
	public void init() {
		for (HashMap<String, Command> category : Commands.rootCommandsCategorized.values()) {
			for(Command command : category.values()) {
				commands.put(command.getName(), command);
			}
		}
	}
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		Map<String, Command> guildCommands = Commands.guildCommands.get(message.getGuild().getIdLong());
		message.getChannel().sendMessage(new EmbedBuilder().setTitle("Waiting...").build()).queue(response -> {
			PageMessage pm = new PageMessage(response, new ArgRunnable<Integer>() {

				@Override
				public void run() {
					response.editMessage(createEmbed(message, response, getArg(), new ArrayList<Command>(guildCommands.values())).build()).queue();;
				}
				
			});
			pm.update();
		});
	}

}
