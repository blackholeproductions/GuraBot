package net.celestialgaze.GuraBot.commands.classes.settings;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Guild;

public class PositiveLongSetting extends CommandModuleSetting<Long> {

	public PositiveLongSetting(CommandModule module, String name, Long defaultValue, boolean editable) {
		super(module, name, defaultValue, editable);
	}

	@Override
	public boolean trySet(Guild guild, String input) {
		if (SharkUtil.canParseLong(input)) {
			long l = SharkUtil.parseLong(input);
			if (validate(guild, l)) {
				this.set(guild, l);
				return true;
			}
		} else {
			if (input.equalsIgnoreCase("max")) {
				this.set(guild, Long.MAX_VALUE);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validate(Guild guild, Long newValue) {
		return newValue >= 0;
	}

	@Override
	public String getInvalidInputMessage(Guild guild, String invalidInput) {
		return "Value must be a long between `0` and `" + Long.MAX_VALUE + "` or `max` for the maximum possible value.";
	}

	@Override
	protected String getInvalidMessage(Guild guild, Long invalidValue) {
		return "The value `" + invalidValue + "` was not a long between `0` and `" + Long.MAX_VALUE + "` in the setting `" + name +
				"` in `" + guild.getName() + "` within the `" + module.getName() + "` module.";
	}

	@Override
	protected String display(Guild guild, Long value) {
		return Long.toString(value);
	}

}

