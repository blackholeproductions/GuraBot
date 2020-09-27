package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class HelpCommand extends Command {

	public HelpCommand(Pair<CommandOptions, Boolean> pair, String helpMenuName) {
		super(pair, false);
		if (commands != null) {
			for (Command cmd : commands.values()) {
				this.commands.put(cmd.getName(), cmd);
			}
		}
		this.helpMenuName = helpMenuName;
		init();
	}

	protected Map<String, Command> commands = new HashMap<String, Command>();
	protected String helpMenuName = "";
	
	public abstract void init();

	public EmbedBuilder createMenu(Message message, String[] args, String[] modifiers) {
		String serverPrefix = (message.getChannelType().equals(ChannelType.TEXT) ? 
				ServerInfo.getServerInfo(message.getGuild().getIdLong()).getProperty(ServerProperty.PREFIX, Commands.defaultPrefix) :
				Commands.defaultPrefix);
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle(helpMenuName + " Help Menu")
				.setColor(GuraBot.DEFAULT_COLOR);
		Map<String, ArrayList<Command>> categories = new HashMap<String, ArrayList<Command>>();
		for (Command command : commands.values()) {
			if (!categories.containsKey(command.getCategory())) {
				categories.put(command.getCategory(), new ArrayList<Command>());
			}
			categories.get(command.getCategory()).add(command);
		}
		for (int i = 0; i < categories.size(); i++) {
			BulletListBuilder blb = new BulletListBuilder();
			String categoryName = new ArrayList<>(categories.keySet()).get(i);
			for (Command command : categories.get(categoryName)) {
				if (command.canRun(message))
					blb.add("**"+serverPrefix+(commands.equals(subcommands) ? ((Subcommand)command).getParent().getName() + " " : "")
							+command.getName(), (!command.getUsage().isEmpty() ? " " + command.getUsage() + "**\n " : ":** ")
							+ command.getDescription(), "");
			}
			
			if (categories.size() > 1) {
				eb.addField(categoryName, blb.build(), false);
			} else {
				eb.setDescription(blb.build());
			}
		}
		return eb;
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage(createMenu(message, args, modifiers).build()).queue();
	}
	
}
