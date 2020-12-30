package net.celestialgaze.GuraBot.commands.modules.counting;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.classes.SettingsCmd;
public class Counting extends HelpCommand {

	public Counting() {
		super(new CommandOptions("counting", "Manage the counting module")
				.setUsage("<subcommand>")
				.setUsablePrivate(false)
				.setCategory("Fun")
				.verify(), "Counting");
	}

	@Override
	public void commandInit() {
		CountingSetChannel setChannel = new CountingSetChannel(this);
		SettingsCmd settings = new SettingsCmd(module, this);
		CountingFix fix = new CountingFix(this);
		addSubcommand(setChannel);
		addSubcommand(settings);
		addSubcommand(fix);
	}

}
