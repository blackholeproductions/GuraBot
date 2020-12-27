package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.RunnableListener;

public abstract class CommandModule {
	protected ModuleType type;
	protected Map<String, Command> commands = new HashMap<String, Command>();
	public CommandModule(ModuleType type, Command... commands) {
		this.type = type;
		for (Command command : commands) {
			command.setModule(this);
			this.commands.put(command.getName(), command);
		}
	}
	public static boolean isEnabled(ModuleType type, long guild) {
		ServerInfo si = ServerInfo.getServerInfo(guild); // Maybe cache whether modules are enabled or not in the future in ServerInfo
		Map<String, Boolean> m = si.getProperty(ServerProperty.MODULES, new LinkedHashMap<String, Boolean>());
		return m.getOrDefault(type.getModName(), false);
	}
	
	public void init() {
		GuraBot.jda.addEventListener(getListener());
	}
	
	public Map<String, Command> getCommands() {
		return commands;
	}
	public List<Command> getCommandsList() {
		return new ArrayList<Command>(commands.values());
	}
	public ModuleType getType() {
		return type;
	}
	public String getName() {
		return type.getModName();
	}
	public String getDescription() {
		return type.getDescription();
	}
	public String getCategory() {
		return type.getCategory();
	}
	public boolean isEnabled(long guild) {
		return CommandModule.isEnabled(type, guild);
	}
	public void setDisabled(long guild) {
		ServerInfo si = ServerInfo.getServerInfo(guild);
		Map<String, Boolean> m = si.getProperty(ServerProperty.MODULES, new LinkedHashMap<String, Boolean>());
		if (m.containsKey(getName())) {
			m.remove(getName());
			si.setProperty(ServerProperty.MODULES, m);
		}
	}
	public void setEnabled(long guild) {
		ServerInfo si = ServerInfo.getServerInfo(guild);
		Map<String, Boolean> m = si.getProperty(ServerProperty.MODULES, new LinkedHashMap<String, Boolean>());
		m.put(getName(), true);
		si.setProperty(ServerProperty.MODULES, m);
	}
	public DocBuilder getModuleDocument(long guild) {
		ServerInfo si = ServerInfo.getServerInfo(guild);
		return new DocBuilder(si.getModuleDocument(type.getTechName()));
	}
	public SubDocBuilder getSettings(long guild) {
		return getModuleDocument(guild).getSubDoc("settings");
	}
	
	public <T> T getSetting(long guild, String setting, T defaultValue) {
		return getSettings(guild).get(setting, defaultValue);
	}
	
	public void setSetting(long guild, String setting, Object value) {
		getSettings(guild).put(setting, value);
	}
	
	public abstract RunnableListener getListener();
}
