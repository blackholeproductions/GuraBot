package net.celestialgaze.GuraBot.util;

import java.util.HashMap;
import java.util.Map;

public class XPUtil {
	static Map<Integer, Long> levels = new HashMap<Integer, Long>();
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
