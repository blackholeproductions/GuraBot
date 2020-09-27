package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class Subcommand extends Command implements ISubcommand {
	public Subcommand(Pair<CommandOptions, Boolean> pair, Command parent) {
		super(pair);
		this.parent = parent;
	}

	private Command parent;

	public Command getParent() {
		return parent;
	}
}
