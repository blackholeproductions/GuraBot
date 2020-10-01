package net.celestialgaze.GuraBot.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class ServerInfo {
	public final Guild guild;
	public static ServerInfo getServerInfo(long id) {
		return new ServerInfo(id);
	}
	public Document getDocument() {
		Document result = GuraBot.servers.find(documentFilter).first();
		if (result == null) {
			GuraBot.servers.insertOne(new Document().append("id", id));
			result = new Document().append("id", id);
		}
		return result;
	}
	long id;
	Bson documentFilter;
	
	public ServerInfo(long id) {
		this.id = id;
		this.documentFilter = Filters.eq("id", id);
		this.guild = GuraBot.jda.getGuildById(id);
	}
	
	public void setProperty(ServerProperty property, Object value) {
        Bson updateOperation = Updates.set(property.toString().toLowerCase(), value);
        GuraBot.servers.updateOne(documentFilter, updateOperation);
	}
	public Document getModuleDocument(String moduleName) {
		Document moduleDoc = getProperty(ServerProperty.MODULE, new Document());
		if (moduleDoc.get(moduleName) == null) {
			moduleDoc.append(moduleName, new Document());
		}
		setProperty(ServerProperty.MODULE, moduleDoc);
		return (Document) moduleDoc.get(moduleName);
	}
	public void updateModuleDocument(String moduleName, Document newDoc) {
		Document moduleDoc = getProperty(ServerProperty.MODULE, new Document());
		moduleDoc.put(moduleName, newDoc);
		setProperty(ServerProperty.MODULE, moduleDoc);
	}
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ServerProperty property) {
		return (T) getDocument().get(property.toString().toLowerCase());
	}
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ServerProperty property, T def) {
		T result = (T) getDocument().get(property.toString().toLowerCase());
		return (result != null ? result : def);
	}
	public String getPrefix() {
		return getProperty(ServerProperty.PREFIX, Commands.defaultPrefix);
	}
	public void setXP(long userId, long value) {
		Document xpDoc = getModuleDocument("xp");
		if (xpDoc.get("experience") == null) xpDoc.put("experience", new Document());
		Document expDoc = (Document) xpDoc.get("experience");
		expDoc.append(Long.toString(userId), value);
		updateModuleDocument("xp", xpDoc);
	}
	public void addXP(long userId, long value) {
		setXP(userId, getXP(userId)+value);
	}
	public long getXP(long userId) {
		Document xpDoc = getModuleDocument("xp");
		if (xpDoc.get("experience") == null) xpDoc.put("experience", new Document());
		Document expDoc = (Document) xpDoc.get("experience");
		Object result = expDoc.get(Long.toString(userId));
		if (result == null) result = Long.parseLong("0");
		return (long) result;
	}
	public Map<String, Long> getXpMap() {
		Map<String, Long> result = new HashMap<>();
		Document xpDoc = getModuleDocument("xp");
		if (xpDoc.get("experience") == null) xpDoc.put("experience", new Document());
		Document expDoc = (Document) xpDoc.get("experience");
		expDoc.forEach((key, value) -> {
			result.put((String)key, (Long)value);
		});
		return result;
	}
	public EmbedBuilder getLeaderboard(int page) {
		return getLeaderboard(page, false);
	}
	public EmbedBuilder getLeaderboard(int page, boolean bots) {
		final int pageSize = 10;
		final int startPosition = (page-1)*pageSize;
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(guild.getName(), null, guild.getIconUrl())
				.setTitle("Leaderboard (Page " + page + ")")
				.setColor(GuraBot.DEFAULT_COLOR);

		Map<String, Long> m = getXpMap();
		ArrayList<String> mKeys = new ArrayList<>(m.keySet());
		Map<Member, Long> sorted = new LinkedHashMap<Member, Long>();
		// Sort by XP
		m.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		  .filter(entry -> {
			  int i = mKeys.indexOf(entry.getKey());
			  if (i >= startPosition && i <= page*pageSize) {
				  Member member = guild.getMemberById(entry.getKey());
				  if (member != null) System.out.println(!member.getUser().isBot());
				  if (member != null && !member.getUser().isBot()) {
					  return true;
				  }
			  }
			  return false;
		  }).forEach(entry -> {
			 sorted.put(guild.getMemberById(entry.getKey()), entry.getValue()); 
		  });
		List<Entry<Member, Long>> entries = new ArrayList<>(sorted.entrySet());
		String description = "";
		for (int i = 0; i < sorted.size(); i++) {
			if (i == 0 && entries.get(i) != null) eb.setThumbnail(entries.get(i).getKey().getUser().getEffectiveAvatarUrl());
			description += (i == 0 ? "**" : "") + (i+1) + ". " + entries.get(i).getKey().getAsMention() + " - " +
			entries.get(i).getValue() + " xp (Level " + XPUtil.getLevel(entries.get(i).getValue()) + ")" + (i == 0 ? "**" : "") + "\n";
		}
		eb.setDescription(description);
		return eb;
	}
}
