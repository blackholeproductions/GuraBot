package net.celestialgaze.GuraBot.commands.modules.xp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.DelayedRunnable;
import net.celestialgaze.GuraBot.util.RunnableListener;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class XpModule extends CommandModule {
	public XpModule(Command... commands) {
		super(ModuleType.XP, commands);
	}

	public RunnableListener getListener() {
		return new RunnableListener() {
			List<Long> cooldowns = new ArrayList<Long>(); // Users that currently have a cooldown for XP
			List<Long> leaderboardCooldowns = new ArrayList<Long>(); // Servers currently on a cooldown for leaderboard updates
			@Override
			public void run() {
				if (currentEvent instanceof MessageReceivedEvent) {
					MessageReceivedEvent event = (MessageReceivedEvent) currentEvent;
					if (!event.getChannelType().equals(ChannelType.TEXT)) return;
					if (!CommandModule.isEnabled(ModuleType.XP, event.getGuild().getIdLong())) return; // If not enabled or not in guild, don't run 
					Long userId = event.getAuthor().getIdLong();
					if (!cooldowns.contains(userId)) { // If user isn't on cooldown
						ServerInfo si = ServerInfo.getServerInfo(event.getGuild().getIdLong());
						
						// Return if channel has disabled xp
						Document xpDoc = si.getModuleDocument(ModuleType.XP.getTechName());
						SubDocBuilder sdbSettings = new DocBuilder(xpDoc).getSubDoc("settings");
						SubDocBuilder sdbToggle = sdbSettings.getSubDoc("toggle");
						
						String type = sdbToggle.get("mode", "whitelist");
						List<String> greylist = sdbToggle.get("list", new ArrayList<String>());
						if (greylist.size() > 0) {
							if (greylist.contains(Long.toString(event.getChannel().getIdLong()))) {
								if (type.equalsIgnoreCase("blacklist")) return;
							} else {
								if (type.equalsIgnoreCase("whitelist")) return;
							}
						}
						
						Document rolesDoc = sdbSettings.get("roles", new Document());
						int currentXP = si.getXP(userId, xpDoc);
						int random = 20+new Random().nextInt(5);
						List<String> badRoles = new ArrayList<String>();
						if (XPUtil.getLevel(currentXP) < XPUtil.getLevel(currentXP + random)) {
							event.getChannel().sendMessage(event.getAuthor().getName() + " is now Level " + XPUtil.getLevel(currentXP + random)).queue();
							rolesDoc.forEach((level, roleId) -> {
								if (XPUtil.getLevel(currentXP + random) >= Integer.parseInt(level) && !event.getMember().getRoles().contains(roleId)) {
									if (event.getGuild().getRoleById((String) roleId) != null) {
										try {
											event.getGuild().addRoleToMember(userId, event.getGuild().getRoleById((String) roleId)).queue();
										} catch (Exception e) {
											String roleName = event.getGuild().getRoleById((String) roleId).getName();
											SharkUtil.sendOwner(event.getGuild(), "Hello! I don't seem to have permissions " +
												"to add " + roleName + " to " + event.getAuthor().getAsTag() + ". My role might " +
												"not be above " + roleName + " in the hierarchy, or I don't have enough permission.");
										}
									} else {
										badRoles.add(level);
										SharkUtil.sendOwner(event.getGuild(), "Hello! I was unable to find a role " +
												"with role ID " + roleId + " that you set for level " + level + ". I'm " +
												"gonna remove it, just thought I'd give you a heads up in case this " +
												"was an accident.");
									}
								}
							});
						}
						if (badRoles.size() > 0) {
							badRoles.forEach((level) -> {
								rolesDoc.remove(level);
							});
							si.updateModuleDocument(ModuleType.XP.getTechName(), sdbSettings.put("roles", rolesDoc).build());
						}
						// Add XP and cooldown
						si.addXP(userId, random); // Add xp
						cooldowns.add(userId);
						new DelayedRunnable(new Runnable() {
								@Override
							public void run() {
								cooldowns.remove(userId);
							}
							
						}).execute(System.currentTimeMillis()+(30*1000)); // Remove after 30 seconds
						
						// If the server has a leaderboard message, update it if it is not on cooldown
						SubDocBuilder sdbLeaderboard = sdbSettings.getSubDoc("leaderboard");
						if (sdbLeaderboard.has("message") && sdbLeaderboard.has("channel")
								&& !leaderboardCooldowns.contains(event.getGuild().getIdLong())) {
							
							// Check for missing channel
							if (event.getGuild()
									.getTextChannelById(sdbLeaderboard.get("channel", (long)0)) == null) {
								SharkUtil.sendOwner(event.getGuild(), 
					        			"Hey! I wasn't able to find the leaderboard message's channel, so I've reset it. " + 
					        			"Make sure to set it again if this wasn't intentional.");
								si.updateModuleDocument(ModuleType.XP.getTechName(), sdbLeaderboard.remove("message").remove("channel").build());
								return;
							}
							
							// Check for missing message
							if (event.getGuild()
									.getTextChannelById(sdbLeaderboard.get("channel", (long)0))
									.retrieveMessageById(sdbLeaderboard.get("message", (long)0)) == null) {
								SharkUtil.sendOwner(event.getGuild(), 
					        			"Hey! I wasn't able to find the leaderboard message, so I've reset it. " + 
					        			"Make sure to set it again if this wasn't intentional.");
								si.updateModuleDocument(ModuleType.XP.getTechName(), sdbLeaderboard.remove("message").remove("channel").build());
								return;
							}
							
							// Attempt to update leaderboard
							event.getGuild()
								.getTextChannelById(sdbLeaderboard.get("channel", (long)0))
								.retrieveMessageById(sdbLeaderboard.get("message", (long)0))
								.queue(message -> {
									long guildId = message.getGuild().getIdLong();
									try {
										message.editMessage(XPUtil.getLeaderboard(message.getGuild(), 1, xpDoc).setTimestamp(Instant.now()).build()).queue();
									} catch (Exception e) {
										SharkUtil.sendOwner(event.getGuild(), 
							        			"Had an error updating your leaderboard. Please check that I have enough permissions. I've reset it " +
												"so you'll have to set it again.");
										si.updateModuleDocument(ModuleType.XP.getTechName(), sdbLeaderboard.remove("message").remove("channel").build());
									}
									leaderboardCooldowns.add(guildId);
									new DelayedRunnable(new Runnable() {
										@Override
										public void run() {
											leaderboardCooldowns.remove(guildId);
										}
										
									}).execute(System.currentTimeMillis()+(30*1000)); // Remove after 30 seconds
								});
						}
					}
				} else if (currentEvent instanceof TextChannelDeleteEvent) {
					TextChannelDeleteEvent event = (TextChannelDeleteEvent)currentEvent;
					ServerInfo si = ServerInfo.getServerInfo(event.getGuild().getIdLong());
					SubDocBuilder sdb = new DocBuilder(si.getModuleDocument(ModuleType.XP.getTechName()))
							.getSubDoc("settings")
							.getSubDoc("toggle");
					
					// Remove deleted channel from xp greylist
					List<String> greylist = sdb.get("list", new ArrayList<String>());
					String channelStrID = Long.toString(event.getChannel().getIdLong());
					if (greylist.contains(channelStrID)) {
						greylist.remove(channelStrID); // Remove deleted channel
						si.updateModuleDocument(ModuleType.XP.getTechName(), sdb.put("list", greylist).build());
					}
				}
			}
		};
	}
}
