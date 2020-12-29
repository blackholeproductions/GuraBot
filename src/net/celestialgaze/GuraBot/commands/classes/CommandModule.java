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
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Guild;

public abstract class CommandModule {
	protected ModuleType type;
	protected Map<String, Command> commands = new HashMap<String, Command>();
	public CommandModuleSettings settings = new CommandModuleSettings(this);
	
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
	
	public <T> T getSetting(long guild, String setting, T defaultValue) {
		return getSettings(guild).get(setting, defaultValue);
	}
	
	public void setSetting(long guild, String setting, Object value) {
		ServerInfo si = ServerInfo.getServerInfo(guild);
		SubDocBuilder settings = getSettings(guild).put(setting, value);
		si.updateModuleDocument(type.getTechName(), settings.build());
	}
	
	public void resetSetting(Guild guild, String setting) {
		resetSetting(guild, setting, true);
	}
	
	public void resetSetting(Guild guild, String setting, boolean notify) {
		switch(settings.getDefaultValidation(setting)) {
		case BOOLEAN:
			setSetting(guild.getIdLong(), setting, settings.getBooleanSetting(setting));
			break;
		case ID:
			setSetting(guild.getIdLong(), setting, settings.getIdSetting(setting));
			break;
		case INTEGER:
			setSetting(guild.getIdLong(), setting, settings.getIntSetting(setting));
			break;
		case LONG:
			setSetting(guild.getIdLong(), setting, settings.getLongSetting(setting));
			break;
		case STRING:
			setSetting(guild.getIdLong(), setting, settings.getStringSetting(setting));
			break;
		default:
			break;
		}
		SharkUtil.sendOwner(guild, "Looks like your " + setting + " setting was invalid, I've reset it for you.");
	}
	
	public abstract RunnableListener getListener();
	public abstract void setupSettings();
}
