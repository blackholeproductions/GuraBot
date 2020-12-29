package net.celestialgaze.GuraBot.commands.classes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.celestialgaze.GuraBot.util.SharkUtil;

public class CommandModuleSettings {
	
	CommandModule module;
	
	public CommandModuleSettings(CommandModule module) {
		this.module = module;
	}
	
	Map<String, String> stringSettings = new HashMap<String, String>();
	Map<String, Boolean> booleanSettings = new HashMap<String, Boolean>();
	Map<String, Integer> integerSettings = new HashMap<String, Integer>();
	Map<String, Long> longSettings = new HashMap<String, Long>();
	Map<String, Long> idSettings = new HashMap<String, Long>();
	
	public void addIntSetting(String setting, int defaultValue) {
		integerSettings.put(setting, defaultValue);
	}
	
	public void addBooleanSetting(String setting, boolean defaultValue) {
		booleanSettings.put(setting, defaultValue);
	}
	
	public void addStringSetting(String setting, String defaultValue) {
		stringSettings.put(setting, defaultValue);
	}
	
	public void addLongSetting(String setting, long defaultValue) {
		longSettings.put(setting, defaultValue);
	}
	
	public void addIdSetting(String setting) {
		idSettings.put(setting, (long)0);
	}
	
	public int getIntSetting(String setting) {
		return integerSettings.get(setting);
	}
	
	public boolean getBooleanSetting(String setting) {
		return booleanSettings.get(setting);
	}
	
	public String getStringSetting(String setting) {
		return stringSettings.get(setting);
	}
	
	public long getLongSetting(String setting) {
		return longSettings.get(setting);
	}
	
	public long getIdSetting(String setting) {
		return idSettings.get(setting);
	}
	
	private boolean isValidIntegerSetting(String setting, String input) {
		try {
			Integer.parseInt(input);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	private boolean notAStupidBooleanMethod(String s) throws Exception { // Boolean.parseBoolean() thinks "yes" == false
		if (s.equalsIgnoreCase("true")) return true;
		if (s.equalsIgnoreCase("false")) return false;
		throw new Exception();
	}
	private boolean isValidBooleanSetting(String setting, String input) {
		try {
			notAStupidBooleanMethod(input);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private boolean isValidLongSetting(String setting, String input) {
		try {
			Long.parseLong(input);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public Validation getValidation(String setting, String input) {
		if (booleanSettings.containsKey(setting) && isValidBooleanSetting(setting, input)) {
			return Validation.BOOLEAN;
		} else if (integerSettings.containsKey(setting) && isValidIntegerSetting(setting, input)) {
			return Validation.INTEGER;
		} else if (longSettings.containsKey(setting) && isValidLongSetting(setting, input)) {
			return Validation.LONG;
		} else if (idSettings.containsKey(setting)) {
			return Validation.ID;
		} else if (stringSettings.containsKey(setting)) { // String requires no validation as the input is a string to begin with
			return Validation.STRING;
		}
		return Validation.NOT;
	}
	
	public Validation getDefaultValidation(String setting) {
		if (booleanSettings.containsKey(setting)) {
			return Validation.BOOLEAN;
		} else if (integerSettings.containsKey(setting)) {
			return Validation.INTEGER;
		} else if (longSettings.containsKey(setting)) {
			return Validation.LONG;
		} else if (idSettings.containsKey(setting)) {
			return Validation.ID;
		} else if (stringSettings.containsKey(setting)) {
			return Validation.STRING;
		}
		return Validation.NOT;
	}
	
	public boolean isValidSetting(String setting, String input) {
		return getValidation(setting, input) != Validation.NOT;
	}
	
	public boolean setSetting(long guildId, String setting, String input) {
		if (!isValidSetting(setting, input)) return false;
		switch(getValidation(setting, input)) {
		case BOOLEAN:
			module.setSetting(guildId, setting, Boolean.parseBoolean(input));
			break;
		case INTEGER:
			module.setSetting(guildId, setting, Integer.parseInt(input));
			break;
		case ID:
			if (SharkUtil.getIdFromMention(input) != 0) {
				module.setSetting(guildId, setting, SharkUtil.getIdFromMention(input));
			} else return false;
			break;
		case LONG:
			module.setSetting(guildId, setting, Long.parseLong(input));
			break;
		case STRING:
			module.setSetting(guildId, setting, input);
			break;
		default:
			break;
		}
		return true;
	}
	
	public String getSettingsList(long id) {
		String output = "";
		Iterator<Entry<String, String>> stringIterator = stringSettings.entrySet().iterator();
		while (stringIterator.hasNext()) {
			Entry<String, String> entry = stringIterator.next();
			output += entry.getKey() + ": \"" + module.getSetting(id, entry.getKey(), entry.getValue()) + "\"\n";
		}
		Iterator<Entry<String, Boolean>> booleanIterator = booleanSettings.entrySet().iterator();
		while (booleanIterator.hasNext()) {
			Entry<String, Boolean> entry = booleanIterator.next();
			output += entry.getKey() + ": " + module.getSetting(id, entry.getKey(), entry.getValue()) + "\n";
		}
		Iterator<Entry<String, Integer>> integerIterator = integerSettings.entrySet().iterator();
		while (integerIterator.hasNext()) {
			Entry<String, Integer> entry = integerIterator.next();
			output += entry.getKey() + ": " + module.getSetting(id, entry.getKey(), entry.getValue()) + "\n";
		}
		Iterator<Entry<String, Long>> longIterator = longSettings.entrySet().iterator();
		while (longIterator.hasNext()) {
			Entry<String, Long> entry = longIterator.next();
			output += entry.getKey() + ": " + module.getSetting(id, entry.getKey(), entry.getValue()) + "\n";
		}
		Iterator<Entry<String, Long>> idIterator = idSettings.entrySet().iterator();
		while (idIterator.hasNext()) {
			Entry<String, Long> entry = idIterator.next();
			output += entry.getKey() + ": ID(" + module.getSetting(id, entry.getKey(), entry.getValue()) + ")\n";
		}
		if (output.isEmpty()) output = "No settings found";
		return output;
	}
	
	public enum Validation {
		NOT, BOOLEAN, INTEGER, STRING, LONG, ID;
	}
}
