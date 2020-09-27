package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
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
		commands = new HashMap<>(Commands.rootCommands);
		for (Command cmd : commands.values()) {
			System.out.println(cmd.getName());
		}
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		EmbedBuilder eb = createMenu(message, args, modifiers);
		Map<String, Command> guildCommands = Commands.guildCommands.get(message.getGuild().getIdLong());
		BulletListBuilder blb = new BulletListBuilder(true);
		for (Command command : guildCommands.values()) {
			blb.add(ServerInfo.getServerInfo(message.getGuild().getIdLong()).getProperty(ServerProperty.PREFIX, Commands.defaultPrefix)+
					command.getName(), command.getDescription());
		}
		if (!blb.build().isEmpty()) {
			eb.addField("Server Commands", blb.build(), false);
		}
		message.getChannel().sendMessage(eb.build()).queue();
	}

}
