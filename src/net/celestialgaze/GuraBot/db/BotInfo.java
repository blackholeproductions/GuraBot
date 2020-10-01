package net.celestialgaze.GuraBot.db;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

import net.celestialgaze.GuraBot.GuraBot;
public class BotInfo {
	public static final Bson FILTER = Filters.eq("name", "stats");
	@SuppressWarnings("unchecked")
	public static <T> T getStat(BotStat type) {
		return (T) GuraBot.bot.find(FILTER).first().get(type.toString().toLowerCase());
	}
	public static int getIntStat(BotStat type) {
		Document doc = GuraBot.bot.find(FILTER).first();
		if (doc.get(type.toString().toLowerCase()) == null) return 0;
		return (int) doc.getInteger(type.toString().toLowerCase());
	}
	public static void addIntStat(BotStat type) {
		addIntStat(type, 1);
	}
	
	public static void addIntStat(BotStat type, int value) {
		setStat(type, getIntStat(type)+value);
	}
	
	public static void setStat(BotStat type, Object value) {
		Document statsDoc = GuraBot.bot.find(FILTER).first();
		statsDoc.put(type.toString().toLowerCase(), value);
		GuraBot.bot.replaceOne(FILTER, statsDoc);
	}
}
