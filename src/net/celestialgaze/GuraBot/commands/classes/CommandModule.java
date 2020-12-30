package net.celestialgaze.GuraBot.commands.classes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
	public Map<String, CommandModuleSetting<?>> settings = new TreeMap<String, CommandModuleSetting<?>>();
	public static Map<ModuleType, CommandModule> map = new HashMap<ModuleType, CommandModule>();
	
	public CommandModule(ModuleType type, Command... commands) {
		this.type = type;
		for (Command command : commands) {
			command.setModule(this);
			command.init();
			this.commands.put(command.getName(), command);
		}
		map.put(type, this);
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
		onStart();
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
		StringBuilder sb = new StringBuilder();
		settings.entrySet().stream().forEachOrdered(entry -> {
			CommandModuleSetting<?> setting = entry.getValue();
			if (setting.editable) sb.append(setting.getName() + ": " + setting.displayCurrent(guild) + "\n");
		});
		if (sb.toString().isEmpty()) sb.append("No settings found");
		return sb.toString();
	}
	
	protected void addSetting(CommandModuleSetting<?> setting) {
		settings.put(setting.getName().toLowerCase(), setting);
	}
	
	public boolean hasSetting(String settingName) {
		return settings.containsKey(settingName.toLowerCase());
	}
	
	public CommandModuleSetting<?> getSetting(String settingName) {
		return settings.get(settingName.toLowerCase());
	}
	public void onStart() {}
	public abstract RunnableListener getListener();
	public abstract void setupSettings();
}
