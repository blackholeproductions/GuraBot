package net.celestialgaze.GuraBot.json;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.GuraBot;
public class BotInfo {
	public static final String FOLDER = GuraBot.DATA_FOLDER+"bot\\";
	public static <T> T getStat(StatType type) {
		String filename = FOLDER+"stats.json";
		JSONObject jo = JSON.readFile(filename);
		T value = (T) jo.get(type.toString().toLowerCase());
		return value;
	}
	public static void addLongStat(StatType type) {
		addLongStat(type, 1);
	}
	public static void addLongStat(StatType type, long value) {
		if (type.getReturnType().getClass() != long.class.getClass()) return;
		long current = (getStat(type) != null ? getStat(type) : (long)0);
		setStat(type, current+value);
	}
	@SuppressWarnings("unchecked")
	public static void setStat(StatType type, Object value) {
		String filename = FOLDER+"stats.json";
		JSONObject jo = JSON.readFile(filename);
		String typeName = type.toString().toLowerCase();
		jo.put(typeName, value);
		JSON.writeToFile(jo, filename);
	}
}
