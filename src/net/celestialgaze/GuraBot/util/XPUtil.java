package net.celestialgaze.GuraBot.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
			ServerInfo.getServerInfo(guild.getIdLong()).updateModuleDocument("xp", sdb.put("roles", rolesDoc).build());
		}
		if (sb.toString().isEmpty()) return "";
		String[] roles = sb.toString().split(",");
		return roles[roles.length-1];
	}

	public static EmbedBuilder getLeaderboard(Guild guild, int page, Document xpDoc) {
		return getLeaderboard(guild, page, xpDoc, false);
	}
	public static EmbedBuilder getLeaderboard(Guild guild, int page, Document xpDoc, boolean bots) {
		final int pageSize = 10;
		final int startPosition = (page-1)*pageSize;
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(guild.getName(), null, guild.getIconUrl())
				.setTitle("Leaderboard (Page " + page + ")")
				.setColor(GuraBot.DEFAULT_COLOR);
		ServerInfo si = ServerInfo.getServerInfo(guild.getIdLong());
		Map<String, Integer> m = si.getXpMap(xpDoc);
		ArrayList<String> mKeys = new ArrayList<>(m.keySet());
		Map<Member, Integer> sorted = new LinkedHashMap<Member, Integer>();
		// Sort by XP
		m.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		  .filter(entry -> {
			  Member member = guild.getMemberById(entry.getKey());
			  if (member != null && (!member.getUser().isBot() || bots)) {
				  return true;
			  }
			  return false;
		  })
		  .forEach(entry -> {
			 sorted.put(guild.getMemberById(entry.getKey()), entry.getValue()); 
		  });
		List<Entry<Member, Integer>> entries = new ArrayList<>(sorted.entrySet());
		String description = "";
		for (int i = startPosition; i < sorted.size(); i++) {
			Member member = entries.get(i).getKey();
			  if (i >= startPosition && i < page*pageSize) {
					  int level = XPUtil.getLevel(entries.get(i).getValue());
						String roleId = XPUtil.getHighestRole(guild, level, xpDoc);
						if (i == 0 && entries.get(i) != null) eb.setThumbnail(entries.get(i).getKey().getUser().getEffectiveAvatarUrl());
						description += (i == 0 ? "**" : "") + (i+1) + ". " + entries.get(i).getKey().getAsMention() + " - " +
								entries.get(i).getValue() + " xp (Level " + level + ")" + (i == 0 ? "**" : "") +
						" " + (!roleId.isEmpty() ? "<@&" + roleId + ">" : "") + "\n";
			  }
		}
		eb.setDescription(description);
		return eb;
	}
}
