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
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class ServerInfo {
	public static Map<Long, ServerInfo> serverInfos = new HashMap<Long, ServerInfo>();
	public final Guild guild;
	public static ServerInfo getServerInfo(long id) {
		if (serverInfos.containsKey(id)) return serverInfos.get(id);
		return new ServerInfo(id);
	}
	public Document cachedProperties = new Document();
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
		serverInfos.put(this.id, this);
	}
	
	public void setProperty(ServerProperty property, Object value) {
        Bson updateOperation = Updates.set(property.toString().toLowerCase(), value);
        GuraBot.servers.updateOne(documentFilter, updateOperation);
		cachedProperties.put(property.toString().toLowerCase(), value);
	}
	public Document getModuleDocument(String moduleName) {
		Document moduleDoc = getProperty(ServerProperty.MODULE, new Document());
		if (moduleDoc.get(moduleName) == null) {
			moduleDoc.append(moduleName, new Document());
		}
		return (Document) moduleDoc.get(moduleName);
		
	}
	public void updateModuleDocument(String moduleName, Document newDoc) {
		Document moduleDoc = getProperty(ServerProperty.MODULE, new Document());
		moduleDoc.put(moduleName, newDoc);
		setProperty(ServerProperty.MODULE, moduleDoc);
	}
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ServerProperty property) {
		String name = property.toString().toLowerCase();
		if (cachedProperties.containsKey(name)) {
			return (T) cachedProperties.get(name);
		} else {
	        System.out.println("getProperty(" + property.toString().toLowerCase() + ")");
	        new Throwable().printStackTrace(System.out);
			T object = (T) getDocument().get(name);
			cachedProperties.put(name, object);
			return object;
		}
	}
	public <T> T getProperty(ServerProperty property, T def) {
		T result = getProperty(property);
		return (result != null ? result : def);
	}
	public String getPrefix() {
		return getProperty(ServerProperty.PREFIX, Commands.defaultPrefix);
	}
	private void setXP(long userId, long value, Document xpDoc) {
			updateModuleDocument("xp",
				new DocBuilder(xpDoc)
					.getSubDoc("experience")
					.put(Long.toString(userId), value)
					.build());
	}
	public void setXP(long userId, long value) {
		setXP(userId, value, getModuleDocument("xp"));
	}
	public void addXP(long userId, long value) {
		Document xpDoc = getModuleDocument("xp");
		setXP(userId, getXP(userId, xpDoc)+value, xpDoc);
	}
	public long getXP(long userId) {
		return getXP(userId, getModuleDocument("xp"));
	}
	public long getXP(long userId, Document xpDoc) {
		return new DocBuilder(xpDoc)
				.getSubDoc("experience")
				.get(Long.toString(userId), Long.parseLong("0"));
	}
	public Map<String, Long> getXpMap() {
		return getXpMap(getModuleDocument("xp"));
	}
	public Map<String, Long> getXpMap(Document xpDoc) {
		Map<String, Long> result = new HashMap<>();
		Document expDoc = new DocBuilder(xpDoc)
				.getSubDoc("experience")
				.buildThis();
		expDoc.forEach((key, value) -> {
			result.put((String)key, (Long)value);
		});
		return result;
	}
	public EmbedBuilder getLeaderboard(int page) {
		return getLeaderboard(page, false);
	}
	public String getHighestRole(Guild guild, int level) {
		return getHighestRole(guild, level, getModuleDocument("xp"));
	}
	public String getHighestRole(Guild guild, int level, Document xpDoc) {
		final StringBuilder sb = new StringBuilder();
		SubDocBuilder sdb = new DocBuilder(xpDoc).getSubDoc("settings");
		Document rolesDoc = sdb.get("roles", new Document());
		List<String> badRoles = new ArrayList<String>();
		rolesDoc.forEach((currentLevel, roleId) -> {
			if (Integer.parseInt(currentLevel) <= level) {
				if (guild.getRoleById((String) roleId) != null) {
					sb.append(roleId);
				} else {
					SharkUtil.sendOwner(guild, "Hello! I was unable to find a role " +
							"with role ID " + roleId + " that you set for level " + level + ". I'm " +
							"gonna remove it, just thought I'd give you a heads up in case this " +
							"was an accident.");
				}
			}
		});

		if (badRoles.size() > 0) {
			badRoles.forEach((badLevel) -> {
				rolesDoc.remove(badLevel);
			});
			updateModuleDocument("xp", sdb.put("roles", rolesDoc).build());
		}
		return sb.toString();
	}
	public EmbedBuilder getLeaderboard(int page, boolean bots) {
		final int pageSize = 10;
		final int startPosition = (page-1)*pageSize;
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(guild.getName(), null, guild.getIconUrl())
				.setTitle("Leaderboard (Page " + page + ")")
				.setColor(GuraBot.DEFAULT_COLOR);
		Document xpDoc = getModuleDocument("xp");
		Map<String, Long> m = getXpMap(xpDoc);
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
			int level = XPUtil.getLevel(entries.get(i).getValue());
			String roleId = getHighestRole(guild, level, xpDoc);
			if (i == 0 && entries.get(i) != null) eb.setThumbnail(entries.get(i).getKey().getUser().getEffectiveAvatarUrl());
			description += (i == 0 ? "**" : "") + (i+1) + ". " + entries.get(i).getKey().getAsMention() + " - " +
			entries.get(i).getValue() + " xp (Level " + level + ")" + (i == 0 ? "**" : "") +
			" " + (!roleId.isEmpty() ? "<@&" + roleId + ">" : "") + "\n";
		}
		eb.setDescription(description);
		return eb;
	}
}
