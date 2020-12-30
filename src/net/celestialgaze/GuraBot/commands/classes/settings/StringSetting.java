package net.celestialgaze.GuraBot.commands.classes.settings;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.dv8tion.jda.api.entities.Guild;

public class StringSetting extends CommandModuleSetting<String> {

	public StringSetting(CommandModule module, String name, String defaultValue, boolean editable) {
		super(module, name, defaultValue, editable);
	}

	@Override
	public boolean trySet(Guild guild, String input) {
		this.set(guild, input);
		return true;
	}

	@Override
	public boolean validate(Guild guild, String newValue) {
		return true;
	}

	@Override
	public String getInvalidInputMessage(Guild guild, String invalidInput) {
		return "how";
	}

	@Override
	protected String getInvalidMessage(Guild guild, String invalidValue) {
		// TODO Auto-generated method stub
		return "how";
	}

	@Override
	protected String display(Guild guild, String value) {
		// TODO Auto-generated method stub
		return "\"`" + value + "`\"";
	}

}
