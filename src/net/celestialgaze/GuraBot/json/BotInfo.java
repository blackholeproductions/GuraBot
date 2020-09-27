package net.celestialgaze.GuraBot.json;

import net.celestialgaze.GuraBot.GuraBot;
public class BotInfo {
	public static final String FOLDER = GuraBot.DATA_FOLDER+"bot\\";
	public static <T> T getStat(BotStat type) {
		return JSON.read(FOLDER+"stats.json", type.toString().toLowerCase());
	}
	
	public static void addLongStat(BotStat type) {
		addLongStat(type, 1);
	}
	
	public static void addLongStat(BotStat type, long value) {
		long current = (getStat(type) != null ? getStat(type) : (long)0);
		setStat(type, current+value);
	}
	
	public static void setStat(BotStat type, Object value) {
		JSON.write(FOLDER+"stats.json", type.toString().toLowerCase(), value);
	}
}
