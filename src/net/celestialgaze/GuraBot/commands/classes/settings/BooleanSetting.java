package net.celestialgaze.GuraBot.commands.classes.settings;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.dv8tion.jda.api.entities.Guild;

public class BooleanSetting extends CommandModuleSetting<Boolean> {

	public BooleanSetting(CommandModule module, String name, Boolean defaultValue, boolean editable) {
		super(module, name, defaultValue, editable);
	}

	@Override
	public boolean trySet(Guild guild, String input) {
		if (input.equalsIgnoreCase("true")) {
			this.set(guild, true);
			return true;
		} else if (input.equalsIgnoreCase("false")) {
			this.set(guild, false);
			return true;
		}
		return false;
	}

	@Override
	public boolean validate(Guild guild, Boolean newValue) {
		return true;
	}

	@Override
	protected String getInvalidMessage(Guild guild, Boolean invalidValue) {
		return "The setting " + name + " was invalid.";
	}

	@Override
	protected String display(Guild guild, Boolean value) {
		return value.toString();
	}

	@Override
	public String getInvalidInputMessage(Guild guild, String invalidInput) {
		return "Input must be either `true` or `false`";
	}

}
