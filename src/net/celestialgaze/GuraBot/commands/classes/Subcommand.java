package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class Subcommand extends Command implements ISubcommand {
	public Subcommand(Pair<CommandOptions, Boolean> pair, Command parent) {
		super(pair, false);
		this.parent = parent;
		if (!pair.getLeft().usablePrivatelySet) this.usablePrivately = parent.usablePrivately;
		if (!pair.getLeft().needBotAdminSet) this.needBotAdmin = parent.needBotAdmin;
		if (pair.getLeft().permission == null) this.permission = parent.permission;
	}

	protected Command parent;
	
	public Command getParent() {
		return parent;
	}
	int i = 0;
	public String getParentName(String before) {
		String totalCmdNames = parent.name+(!before.isEmpty() ? " " : "")+before;
		if (parent instanceof ISubcommand) {
			return ((ISubcommand)parent).getParentName(totalCmdNames);
		}
		return totalCmdNames;
	}
	public String getParentName() {
		return getParentName("");
	}
}
