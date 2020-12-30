package net.celestialgaze.GuraBot.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class XPUtil {
	static Map<Integer, Long> levels = new HashMap<Integer, Long>();
	private static void genLevels() {
		if (levels.size() == 0) {
			long time = System.currentTimeMillis();
			for (int i = -1000; i <= 1000; i++) {	
				levels.put(i, Math.round(Math.floor(2.41*Math.pow(i, 3)-39*Math.pow(i, 2)+(357*i)-320.41)));
			}
			System.out.println("Calculated " + levels.size() + " levels (" + (System.currentTimeMillis()-time) + "ms)");
		}
	}
	public static int getLevel(long experience) {
		int level = 0;
		genLevels();
		for (int i = -1000; i <= 1000; i++) {
			if (levels.get(i) <= experience) {
				level = i;
			} else {
				break;
			}
		}
		return level;
	}
	public static long getExpForLevel(int level) {
		return levels.get(level+1) - levels.get(level);
	}
	public static long getExpInLevel(long experience) {
		return experience - levels.get(getLevel(experience));
	}
	public static String getHighestRole(Guild guild, int level) {
		return getHighestRole(guild, level, ServerInfo.getServerInfo(guild.getIdLong()).getModuleDocument("xp"));
	}
	public static String getHighestRole(Guild guild, int level, Document xpDoc) {
		final StringBuilder sb = new StringBuilder();
		SubDocBuilder sdb = new DocBuilder(xpDoc).getSubDoc("settings");
		Document rolesDoc = sdb.get("roles", new Document());
		List<String> badRoles = new ArrayList<String>();
		rolesDoc.forEach((currentLevel, roleId) -> {
			if (Integer.parseInt(currentLevel) <= level) {
				if (guild.getRoleById((String) roleId) != null) {
					sb.append(roleId+",");
				} else {
					badRoles.add(currentLevel);
					SharkUtil.sendOwner(guild, "Hello! I was unable to find a role " +
							"with role ID " + roleId + " that you set for level " + currentLevel + ". I'm " +
							"gonna remove it, just thought I'd give you a heads up in case this " +
							"was an accident.");
				}
			}
		});

		if (badRoles.size() > 0) {
			badRoles.forEach((badLevel) -> {
				rolesDoc.remove(badLevel);
			});
			ServerInfo.getServerInfo(guild.getIdLong()).updateModuleDocument("xp", sdb.put("roles", rolesDoc).build());
		}
		if (sb.toString().isEmpty()) return Long.toString(guild.getPublicRole().getIdLong()); // Return public role if none found
		String[] roles = sb.toString().split(",");
		return roles[roles.length-1];
	}

	public static EmbedBuilder getLeaderboard(Guild guild, int page, Document xpDoc) {
		return getLeaderboard(guild, page, xpDoc, false, false);
	}
	public static EmbedBuilder getLeaderboard(Guild guild, int page, Document xpDoc, boolean bots) {
		return getLeaderboard(guild, page, xpDoc, bots, false);
	}
	public static EmbedBuilder getLeaderboard(Guild guild, int page, Document xpDoc, boolean bots, boolean usernames) {
		return getLeaderboard(guild, page, xpDoc, bots, usernames, false);
	}
	public static EmbedBuilder getLeaderboard(Guild guild, int page, Document xpDoc, boolean bots, boolean usernames, boolean left) {
		final int pageSize = 10;
		final int startPosition = (page-1)*pageSize;
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(guild.getName(), null, guild.getIconUrl())
				.setTitle("Leaderboard (Page " + page + ")")
				.setColor(GuraBot.DEFAULT_COLOR);
		ServerInfo si = ServerInfo.getServerInfo(guild.getIdLong());
		Map<String, Integer> m = si.getXpMap(xpDoc, bots, left);
		Map<String, Integer> sorted = new LinkedHashMap<String, Integer>();
		// Sort by XP
		m.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		  .forEach(entry -> {
			 sorted.put(entry.getKey(), entry.getValue()); 
		  });
		List<Entry<String, Integer>> entries = new ArrayList<>(sorted.entrySet());
		String description = "";
		for (int i = startPosition; i < sorted.size(); i++) {
			  if (i >= startPosition && i < page*pageSize) {
				  int level = XPUtil.getLevel(entries.get(i).getValue());
				  String roleId = XPUtil.getHighestRole(guild, level, xpDoc);
				  Member member = guild.getMemberById(entries.get(i).getKey());
				  if (i == 0 && member != null) eb.setThumbnail(member.getUser().getEffectiveAvatarUrl());
				  description += (i == 0 ? "**" : "") + (i+1) + ". " + 
						  (member != null ? (usernames ? member.getUser().getAsTag() : member.getAsMention()) : "*Unknown User*") 
						  + " - " + entries.get(i).getValue() + " xp (Level " + level + ")" + (i == 0 ? "**" : "") +
						  " " + (!roleId.contentEquals(guild.getPublicRole().getId()) ? "<@&" + roleId + ">" : "") + "\n";
			  }
		}
		eb.setDescription(description);
		return eb;
	}
	public static int getRank(Guild guild, Member member) {
		return getRank(guild, member, ServerInfo.getServerInfo(guild.getIdLong()).getModuleDocument("xp"));
	}
	
	public static int getRank(Guild guild, Member member, Document xpDoc) {
		ServerInfo si = ServerInfo.getServerInfo(guild.getIdLong());
		Map<String, Integer> m = si.getXpMap(xpDoc, member.getUser().isBot());
		LinkedHashMap<String, Integer> sorted = new LinkedHashMap<String, Integer>();
		// Sort by XP
		m.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		  .forEach((entry) -> {
			 sorted.put(entry.getKey(), entry.getValue()); 
		  });
		int i = 0;
		Iterator<Entry<String, Integer>> iterator = sorted.entrySet().stream().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			i++;
			if (entry.getKey().contentEquals(member.getId())) {
				return i;
			}
		}
		return sorted.size()+1;
	}
}
