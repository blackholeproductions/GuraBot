package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class SubHelpCommand extends HelpCommand implements ISubcommand {

	public SubHelpCommand(Pair<CommandOptions, Boolean> pair, String helpMenuName, Command parent) {
		super(pair, helpMenuName);
		this.parent = parent;
		this.needBotAdmin = parent.needBotAdmin;
		this.permission = parent.permission;
	}
	protected Command parent;
	public Command getParent() {
		return parent;
	}
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
