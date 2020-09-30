package net.celestialgaze.GuraBot.json;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class ServerInfo {
	public final String filename;
	public final Guild guild;
	public static ServerInfo getServerInfo(long id) {
		return new ServerInfo(id);
	}
	public static String getFilename(long id) {
		return GuraBot.DATA_FOLDER + "server\\" + id + ".json";
	}
	long id;
	
	public ServerInfo(long id) {
		this.id = id;
		this.guild = GuraBot.jda.getGuildById(id);
		filename = getFilename(id);
	}
	
	public void setProperty(ServerProperty property, Object value) {
		JSON.write(filename, property.toString().toLowerCase(), value);
	}
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ServerProperty property) {
		return (T) JSON.read(filename, property.toString().toLowerCase());
	}
	public <T> T getProperty(ServerProperty property, T def) {
		return (T) JSON.read(filename, property.toString().toLowerCase(), def);
	}
	public String getPrefix() {
		return getProperty(ServerProperty.PREFIX, Commands.defaultPrefix);
	}
	public void setXP(long userId, long value) {
		XPUtil.setXP(id, userId, value);
	}
	public void addXP(long userId, long value) {
		XPUtil.addXP(id, userId, value);
	}
	public long getXP(long userId) {
		return XPUtil.getXP(id, userId);
	}
	public Map<String, Long> getXpMap() {
		return XPUtil.getXpMap(id);
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
