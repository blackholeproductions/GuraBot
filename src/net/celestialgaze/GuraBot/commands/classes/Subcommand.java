package net.celestialgaze.GuraBot.commands.classes;

public abstract class Subcommand extends Command {
	protected Subcommand(String name, String usage, String description) {
		super(name, usage, description);
	}
	private Command parent;
	public Command getParent() {
		return parent;
	}
}
