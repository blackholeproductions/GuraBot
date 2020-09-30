package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
import net.celestialgaze.GuraBot.util.InteractableMessage;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class HelpCommand extends Command implements IPageCommand {
	
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

	public EmbedBuilder createEmbed(Message userMessage, Message messageToEdit, int page) {
		return createEmbed(userMessage, messageToEdit, page, null);
	}
	public EmbedBuilder createEmbed(Message userMessage, Message messageToEdit, int page, List<Command> extra) {
		int pageSize = PAGE_SIZE;
		int startPosition = (page-1)*pageSize;
		String serverPrefix = (userMessage.getChannelType().equals(ChannelType.TEXT) ? 
				ServerInfo.getServerInfo(userMessage.getGuild().getIdLong()).getProperty(ServerProperty.PREFIX, Commands.defaultPrefix) :
				Commands.defaultPrefix);
		
		// Add the stuff in extra, if any
		if (extra != null) {
			for (Command command : extra) {
				commands.put(command.getName(), command);
			}
		}
		
		// Remove all commands that can't be run
		Map<String, Command> newCommands = new HashMap<String, Command>(commands); // copy so we don't get ConcurrentModificationException
		for (Command command : commands.values()) {
			if (!command.canRun(userMessage, true, false)) {
				newCommands.remove(command.getName());
			}
		}
		commands = newCommands;
		
		int maxPageSize = Math.toIntExact(Math.round(Math.ceil((commands.size()+0.0)/(pageSize+0.0))));
		PageMessage pm = (PageMessage) InteractableMessage.list.get(messageToEdit.getIdLong());
		pm.setMaxSize(maxPageSize);
		
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle(helpMenuName + " Help Menu - Page (" + page + "/" + maxPageSize + ")")
				.setColor(GuraBot.DEFAULT_COLOR);

		// Sort the commands into their categories
		Map<String, Command> commandsSorted = new LinkedHashMap<String, Command>();
		commands
		  .entrySet()
		  .stream()
		  .sorted((e1, e2) -> e1.getValue().getCategory().compareTo(e2.getValue().getCategory()))
		  .forEach(entry -> commandsSorted.put(entry.getKey(), entry.getValue()));
		
		List<Command> commandValues = new ArrayList<>(commandsSorted.values());

		// Make bullet lists for each categories
		Map<String, BulletListBuilder> categoryStrings = new LinkedHashMap<String, BulletListBuilder>();
		for (int i = 0; i < commandsSorted.size(); i++) {
			if (i < startPosition) continue;
			if (i > startPosition+pageSize-1) break;
			Command command = commandValues.get(i);
			if (!categoryStrings.containsKey(command.getCategory())) {
				categoryStrings.put(command.getCategory(), new BulletListBuilder());
			}
			categoryStrings.get(command.getCategory()).add("**"+serverPrefix+(commands.equals(subcommands) ? 
					((Subcommand)command).getParent().getName() + " " : "") +command.getName(), 
					(!command.getUsage().isEmpty() ? " " + command.getUsage() + "**\n " : ":** ")
					+ command.getDescription(), "");
		}
		
		// Load them into the embed
		if (categoryStrings.size() == 1) {
			eb.setDescription(((BulletListBuilder) categoryStrings.values().toArray()[0]).build());
		} else if (categoryStrings.size() > 1) {
			for (Entry<String, BulletListBuilder> entry : categoryStrings.entrySet()) {
				eb.addField(entry.getKey(), entry.getValue().build(), false); // 
			}
		} else {
			eb.setDescription("Sorry, no commands were found.");
		}
		return eb;
	}
	
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		SharkUtil.sendHelpMenu(message, this);
	}
	
}
