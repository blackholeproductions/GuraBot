package net.celestialgaze.GuraBot.commands.classes;

public abstract class Subcommand extends Command {
	private Command parent;
	protected Subcommand(String name, String usage, String description, Command parent) {
		super(name, usage, description, false);
		this.parent = parent;
		init();
	}
	public Command getParent() {
		return parent;
	}
}
