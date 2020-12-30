package net.celestialgaze.GuraBot.commands.classes.settings;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.dv8tion.jda.api.entities.Guild;

public class BooleanUserValueSetting extends UserValueSetting<Boolean> {

	public BooleanUserValueSetting(CommandModule module, String name, boolean startingValue) {
		super(module, name, startingValue);
	}

	@Override
	public Boolean getUserValue(Guild guild, long userId) {
		return (this.get(guild).getBoolean(Long.toString(userId)) != null ? this.get(guild).getBoolean(Long.toString(userId)) : startingValue);
	}

	@Override
	public void setUserValue(Guild guild, long userId, Boolean value) {
		this.get(guild).put(Long.toString(userId), value);
	}

	@Override
	public Map<String, Boolean> getMap(Guild guild) {
		Map<String, Boolean> m = new HashMap<String, Boolean>();
		this.get(guild).forEach((string, value) -> {
			m.put(string, (boolean) value);
		});
		return m;
	}

}
