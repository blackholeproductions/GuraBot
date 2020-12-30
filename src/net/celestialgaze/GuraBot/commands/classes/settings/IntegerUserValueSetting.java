package net.celestialgaze.GuraBot.commands.classes.settings;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.dv8tion.jda.api.entities.Guild;

public class IntegerUserValueSetting extends UserValueSetting<Integer> {

	public IntegerUserValueSetting(CommandModule module, String name, Integer startingValue) {
		super(module, name, startingValue);
	}
	
	@Override
	public Integer getUserValue(Guild guild, long userId) {
		return this.get(guild).getInteger(Long.toString(userId), 0);
	}

	@Override
	public void setUserValue(Guild guild, long userId, Integer value) {
		this.get(guild).put(Long.toString(userId), value);
	}

	@Override
	public Map<String, Integer> getMap(Guild guild) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		this.get(guild).forEach((string, value) -> {
			m.put(string, (int) value);
		});
		return m;
	}

}
