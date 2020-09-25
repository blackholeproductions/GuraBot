package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.util.SharkUtil;

public abstract class Command implements ICommand {
	protected String name;
	protected String usage;
	protected String description;
	protected List<Command> subcommands = new ArrayList<Command>();
	Permission permission = null;
	protected boolean usablePrivately = true;
	protected boolean needBotAdmin = false;
	protected Command(String name, String usage, String description) {
		setValues(name, usage, description, false);
	}
	protected Command(String name, String usage, String description, boolean initialize) {
		setValues(name, usage, description, initialize);
	}
	private void setValues(String name, String usage, String description, boolean initialize) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		System.out.println("Initializing " + name + " command");
		if (initialize) init();
	}
	
	public abstract void init();
	@Override
	public void attempt(Message message, String[] args) {
		if (canRun(message, false)) {
			try {
				run(message, args);
			} catch (Exception e) {
				String argsString = "";
				for (String arg : args) {
					argsString += arg + " ";
				}
				argsString = argsString.trim();
				String error = "Something went horribly wrong...\n" + e.getClass().getSimpleName() + ": " + e.getMessage() + "\n" +
						"\n" + "Command ran: " + this.getName() + "\n" + "Subcommand: " + Boolean.toString(this instanceof Subcommand) + 
						(this instanceof Subcommand ? "\nParent: " + ((Subcommand)this).getParent().getName() : "") + "\n" +
						"Args: " + argsString + "\nFull message: " + message.getContentRaw().substring(0, Integer.min(message.getContentRaw().length(), 100));
				SharkUtil.error(message, error);
				message.getChannel().sendMessage("Reporting to cel...").queue(response -> {
					GuraBot.jda.getUserByTag("celestialgaze", "0001").openPrivateChannel().queue(channel -> {
						channel.sendMessage("fix ur bot").queue(crashMsg -> {
							SharkUtil.error(crashMsg, error);
							response.editMessage("Successfully reported to cel.").queue();
						});
					});
				});
				e.printStackTrace();
			}
		}
	}
	public boolean canRun(Message message) {
		return canRun(message, true);
	}
	public boolean canRun(Message message, boolean silent) {
		if (!usablePrivately && message.getChannelType().equals(ChannelType.PRIVATE)) {
			error(message, "Sorry, this command is not available in DMs.", silent);
			return false;
		}
		if (permission != null && message.getChannelType().equals(ChannelType.TEXT) && !message.getMember().hasPermission(permission)) {
			error(message, "You need the " + permission.getName() + " permission to use this command", silent);
			return false;
		}
		if (needBotAdmin && message.getAuthor().getIdLong() != Long.valueOf("218525899535024129")) {
			error(message, "You're not celestialgaze#0001!", silent);
			return false;
		}
		return true;
	}
	private static void error(Message message, String error, boolean silent) {
		if (!silent) SharkUtil.error(message, error);
	}
	protected abstract void run(Message message, String[] args);
	
	public String getName() {
		return name;
	}
	public String getUsage() {
		return usage;
	}
	public String getDescription() {
		return description;
	}
	public boolean isUsablePrivately() {
		return usablePrivately;
	}
	public boolean hasPermission() {
		return permission != null;
	}
	public Permission getPermission() {
		return permission;
	}
	
	public List<Command> getSubcommands() {
		return subcommands;
	}
}
