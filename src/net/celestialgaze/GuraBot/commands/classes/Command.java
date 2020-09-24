package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public abstract class Command implements ICommand {
	protected String name;
	protected String usage;
	protected String description;
	protected List<Command> subcommands = new ArrayList<Command>();
	Permission permission;
	protected Command(String name, String usage, String description) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		System.out.println("Initializing " + name + " command");
		init();
	}
	
	public abstract void init();
	public abstract void run(Message message, String[] args);
	
	public String getName() {
		return name;
	}
	public String getUsage() {
		return usage;
	}
	public String getDescription() {
		return description;
	}
	
	public List<Command> getSubcommands() {
		return subcommands;
	}
}
