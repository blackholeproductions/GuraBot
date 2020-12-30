package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.settings.BooleanSetting;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.ServerProperty;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.RunnableListener;
import net.dv8tion.jda.api.entities.Guild;

public abstract class CommandModule {
	protected ModuleType type;
	protected Map<String, Command> commands = new HashMap<String, Command>();
	public Map<String, CommandModuleSetting<?>> settings = new HashMap<String, CommandModuleSetting<?>>();
	
	public CommandModule(ModuleType type, Command... commands) {
		this.type = type;
		for (Command command : commands) {
			command.setModule(this);
			this.commands.put(command.getName(), command);
		}
		init();
	}
	
	public static boolean isEnabled(ModuleType type, long guild) {
		ServerInfo si = ServerInfo.getServerInfo(guild); // Maybe cache whether modules are enabled or not in the future in ServerInfo
		Map<String, Boolean> m = si.getProperty(ServerProperty.MODULES, new LinkedHashMap<String, Boolean>());
		return m.getOrDefault(type.getModName(), false);
	}
	
	public void init() {
		GuraBot.jda.addEventListener(getListener());
		setupSettings();
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
	
	public String getSettingsList(Guild guild) {
		String s = "";
		for (CommandModuleSetting<?> setting : settings.values()) {
			s += setting.getName() + ": " + setting.displayCurrent(guild) + "\n";
		}
		return s;
	}
	
	protected void addSetting(CommandModuleSetting<?> setting) {
		settings.put(setting.getName(), setting);
	}
	
	public boolean hasSetting(String settingName) {
		return settings.containsKey(settingName);
	}
	
	public CommandModuleSetting<?> getSetting(String settingName) {
		return settings.get(settingName);
	}
	
	public abstract RunnableListener getListener();
	public abstract void setupSettings();
}
