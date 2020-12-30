package net.celestialgaze.GuraBot.commands.modules.counting;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.modules.counting.settings.CountingSettings;
public class Counting extends HelpCommand {

	public Counting() {
		super(new CommandOptions("counting", "Manage the counting module")
				.setUsage("<subcommand>")
				.setUsablePrivate(false)
				.setCategory("Fun")
				.verify(), "Counting");
	}

	@Override
	public void init() {
		CountingSetChannel setChannel = new CountingSetChannel(this);
		CountingSettings settings = new CountingSettings(this);
		CountingFix fix = new CountingFix(this);
		subcommands.put(setChannel.getName(), setChannel);
		subcommands.put(settings.getName(), settings);
		subcommands.put(fix.getName(), fix);
	}

}
