package net.celestialgaze.GuraBot.commands.classes;

public interface ISubcommand {
	public Command getParent();
	public String getParentName(String before);
	public String getParentName();
}
