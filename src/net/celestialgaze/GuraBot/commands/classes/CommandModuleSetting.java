package net.celestialgaze.GuraBot.commands.classes;

import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Guild;

public abstract class CommandModuleSetting<T> {
	protected String name;
	protected T defaultValue;
	protected CommandModule module;
	public CommandModuleSetting(CommandModule module, String name, T defaultValue) {
		this.module = module;
		this.name = name;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}

	public void restore(Guild guild) {
		restore(guild, false);
	}
	
	public void restore(Guild guild, boolean notify) {
		if (notify) SharkUtil.sendOwner(guild, getInvalidMessage(guild, module.getSettings(guild.getIdLong()).get(name, defaultValue)));
		setSetting(guild, name, defaultValue);
	}
	
	public T get(Guild guild) {
		T value = module.getSettings(guild.getIdLong()).get(name, defaultValue);
		if (!validate(guild, value)) {
			restore(guild, true);
			return defaultValue;
		}
		return value;
	}
	
	public void set(Guild guild, T newValue) {
		setSetting(guild, name, newValue);
	}
	
	public String displayCurrent(Guild guild) {
		return display(guild, get(guild));
	}
	
	public abstract boolean trySet(Guild guild, String input);
	public abstract boolean validate(Guild guild, T newValue);
	public abstract String getInvalidInputMessage(Guild guild, String invalidInput);
	protected abstract String getInvalidMessage(Guild guild, T invalidValue);
	protected abstract String display(Guild guild, T value);
	
	private void setSetting(Guild guild, String setting, T value) {
		ServerInfo si = ServerInfo.getServerInfo(guild.getIdLong());
		SubDocBuilder settings = module.getSettings(guild.getIdLong()).put(setting, value);
		si.updateModuleDocument(module.type.getTechName(), settings.build());
	}
}
