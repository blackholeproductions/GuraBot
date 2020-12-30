package net.celestialgaze.GuraBot.commands.classes.settings;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Guild;

public class PositiveIntegerSetting extends CommandModuleSetting<Integer> {

	public PositiveIntegerSetting(CommandModule module, String name, Integer defaultValue) {
		super(module, name, defaultValue);
	}

	@Override
	public boolean trySet(Guild guild, String input) {
		if (SharkUtil.canParseInt(input)) {
			int i = SharkUtil.parseInt(input);
			if (validate(guild, i)) {
				this.set(guild, i);
				return true;
			}
		} else {
			if (input.equalsIgnoreCase("max")) {
				this.set(guild, Integer.MAX_VALUE);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validate(Guild guild, Integer newValue) {
		return newValue >= 0;
	}

	@Override
	public String getInvalidInputMessage(Guild guild, String invalidInput) {
		return "Value must be an integer between `0` and `" + Integer.MAX_VALUE + "` or `max` for the maximum possible value.";
	}

	@Override
	protected String getInvalidMessage(Guild guild, Integer invalidValue) {
		return "The value `" + invalidValue + "` was not an integer between `0` and `" + Integer.MAX_VALUE + "` in the setting `" + name +
				"` in `" + guild.getName() + "` within the `" + module.getName() + "` module.";
	}

	@Override
	protected String display(Guild guild, Integer value) {
		return Integer.toString(value);
	}

}
