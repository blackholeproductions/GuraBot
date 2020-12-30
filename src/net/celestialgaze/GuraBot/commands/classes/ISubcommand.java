package net.celestialgaze.GuraBot.commands.classes;

public interface ISubcommand extends ICommand {
	public Command getParent();
	public String getParentName(String before);
	public String getParentName();
}
