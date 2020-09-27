package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
import net.celestialgaze.GuraBot.util.InteractableMessage;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class HelpCommand extends Command {
	public static final int PAGE_SIZE = 10;
	
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

		if (extra != null) {
			for (Command command : extra) {
				commands.put(command.getName(), command);
			}
		}
		int maxPageSize = Math.toIntExact(Math.round(Math.ceil((commands.size()+0.0)/(PAGE_SIZE+0.0))));
		PageMessage pm = (PageMessage) InteractableMessage.list.get(messageToEdit.getIdLong());
		pm.setMaxSize(maxPageSize);
		
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle(helpMenuName + " Help Menu - Page (" + page + "/" + maxPageSize + ")")
				.setColor(GuraBot.DEFAULT_COLOR);

		List<Command> commandsToDisplay = new ArrayList<Command>();
		// Construct a list of commands to process of size pageSize starting at startPosition
		for (int i = 0; i < commands.size(); i++) {
			if (i < startPosition) continue;
			if (i > startPosition+pageSize-1) break;
			commandsToDisplay.add(new ArrayList<Command>(commands.values()).get(i));
		}
		Map<String, ArrayList<Command>> categories = new HashMap<String, ArrayList<Command>>();
		for (Command command : commandsToDisplay) {
			if (!categories.containsKey(command.getCategory())) {
				categories.put(command.getCategory(), new ArrayList<Command>());
			}
			categories.get(command.getCategory()).add(command);
		}
		
		for (int i = 0; i < categories.size(); i++) {
			BulletListBuilder blb = new BulletListBuilder();
			String categoryName = new ArrayList<>(categories.keySet()).get(i);
			for (Command command : categories.get(categoryName)) {
				if (command.canRun(userMessage))
					blb.add("**"+serverPrefix+(commands.equals(subcommands) ? ((Subcommand)command).getParent().getName() + " " : "")
							+command.getName(), (!command.getUsage().isEmpty() ? " " + command.getUsage() + "**\n " : ":** ")
							+ command.getDescription(), "");
			}
			eb.addField(categoryName, blb.build(), false);
		}
		return eb;
	}
	
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage(new EmbedBuilder().setTitle("Waiting...").build()).queue(response -> {
			PageMessage pm = new PageMessage(response, new ArgRunnable<Integer>() {

				@Override
				public void run() {
					response.editMessage(createEmbed(message, response, getArg()).build()).queue();;
				}
				
			});
			pm.update();
		});
	}
	
}
