package net.celestialgaze.GuraBot.commands.classes.settings;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.dv8tion.jda.api.entities.Guild;

public class IntegerUserValueSetting extends CommandModuleSetting<Document> {

	int startingValue = 0;
	public IntegerUserValueSetting(CommandModule module, String name, int startingValue) {
		super(module, name, new Document(), false);
		this.startingValue = startingValue;
	}
	
	public int getUserValue(Guild guild, long userId) {
		return this.get(guild).getInteger(Long.toString(userId), 0);
	}

	public void setUserValue(Guild guild, long userId, int value) {
		this.get(guild).put(Long.toString(userId), value);
	}
	
	public Map<String, Integer> getMap(Guild guild) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		this.get(guild).forEach((string, value) -> {
			m.put(string, (int) value);
		});
		return m;
	}
	
	@Override
	public boolean trySet(Guild guild, String input) {
		return false;
	}

	@Override
	public boolean validate(Guild guild, Document newValue) {
		return false;
	}

	@Override
	public String getInvalidInputMessage(Guild guild, String invalidInput) {
		return null;
	}

	@Override
	protected String getInvalidMessage(Guild guild, Document invalidValue) {
		return null;
	}

	@Override
	protected String display(Guild guild, Document value) {
		return null;
	}

}
