package net.celestialgaze.GuraBot.commands.classes.settings;

import java.util.Map;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.dv8tion.jda.api.entities.Guild;

public abstract class UserValueSetting<T> extends CommandModuleSetting<Document> {

	T startingValue;
	public UserValueSetting(CommandModule module, String name, T startingValue) {
		super(module, name, new Document(), false);
		this.startingValue = startingValue;
	}
	
	public abstract T getUserValue(Guild guild, long userId);

	public abstract void setUserValue(Guild guild, long userId, T value);
	
	public abstract Map<String, T> getMap(Guild guild);
	
	public void clearValues(Guild guild) {
		this.set(guild, new Document());
	}
	
	@Override
	public boolean trySet(Guild guild, String input) {
		return false;
	}

	@Override
	public boolean validate(Guild guild, Document newValue) {
		return true;
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

