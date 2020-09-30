package net.celestialgaze.GuraBot.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.celestialgaze.GuraBot.json.ServerInfo;
import net.celestialgaze.GuraBot.json.ServerProperty;

public class XPUtil {
	static Map<Integer, Long> levels = new HashMap<Integer, Long>();
	public static void setXP(long guildId, long userId, long value) {
		Map<String, Long> m = getXpMap(guildId);
		m.put(Long.toString(userId), value);
		ServerInfo.getServerInfo(guildId).setProperty(ServerProperty.XP, m);
	}
	public static void addXP(long guildId, long userId, long value) {
		setXP(guildId, userId, getXP(guildId, userId)+value);
	}
	public static long getXP(long guildId, long userId) {
		Map<String, Long> m = getXpMap(guildId);
		return (m.get(Long.toString(userId)) != null ? m.get(Long.toString(userId)) : 0);
	}
	public static Map<String, Long> getXpMap(long guildId) {
		ServerInfo si = ServerInfo.getServerInfo(guildId);
		return si.getProperty(ServerProperty.XP, new LinkedHashMap<String, Long>());
	}
	public static int getLevel(long experience) {
		int level = 0;
		if (levels.size() == 0) {
			long time = System.currentTimeMillis();
			for (int i = 0; i <= 1000; i++) {	
				levels.put(i, Math.round(Math.floor(2.41*Math.pow(i, 3)-39*Math.pow(i, 2)+(357*i)-320.41)));
			}
			System.out.println("Calculated " + levels.size() + " levels (" + (System.currentTimeMillis()-time) + "ms)");
		}
		for (int i = 0; i <= 1000; i++) {
			if (levels.get(i) <= experience) {
				level = i;
			} else {
				break;
			}
		}
		return level;
	}
}
