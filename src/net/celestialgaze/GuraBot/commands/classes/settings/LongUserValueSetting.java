package net.celestialgaze.GuraBot.commands.classes.settings;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.dv8tion.jda.api.entities.Guild;

public class LongUserValueSetting extends UserValueSetting<Long> {

	boolean canBeNegative = true;
	public LongUserValueSetting(CommandModule module, String name, long startingValue, boolean canBeNegative) {
		super(module, name, startingValue);
		this.canBeNegative = canBeNegative;
	}
	
	public Long getUserValue(Guild guild, long userId) {
		return (this.get(guild).getLong(Long.toString(userId)) != null ? this.get(guild).getLong(Long.toString(userId)) : startingValue);
	}

	public void setUserValue(Guild guild, long userId, Long value) {
		if (!canBeNegative && value < 0) value = Long.valueOf("0");
		this.get(guild).put(Long.toString(userId), value);
	}
	
	public Map<String, Long> getMap(Guild guild) {
		Map<String, Long> m = new HashMap<String, Long>();
		this.get(guild).forEach((string, value) -> {
			m.put(string, (long)value);
		});
		return m;
	}
	

}
