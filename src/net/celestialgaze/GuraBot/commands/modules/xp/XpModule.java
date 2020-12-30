package net.celestialgaze.GuraBot.commands.modules.xp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.settings.BooleanSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.ChannelIDSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.PositiveIntegerSetting;
import net.celestialgaze.GuraBot.commands.modules.counting.CountingModule;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.DelayedRunnable;
import net.celestialgaze.GuraBot.util.RunnableListener;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class XpModule extends CommandModule {
	public static XpModule instance;
	public XpModule(Command... commands) {
		super(ModuleType.XP, commands);
		instance = this;
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
					Guild guild = event.getGuild();
					if (!CommandModule.isEnabled(ModuleType.XP, guild.getIdLong())) return; // If not enabled or not in guild, don't run 
					Long userId = event.getAuthor().getIdLong();
					
					// Counting module hook
					if (CommandModule.isEnabled(ModuleType.COUNTING, guild.getIdLong())) { // counting module is enabled
						if (CountingModule.instance.isValidCount(event.getMessage())) { // message is valid counting message
							int xpToGive = CountingModule.instance.xpAwarded.get(guild); // Get amount of xp to give from config
							if (xpToGive > 0) {
								ServerInfo si = ServerInfo.getServerInfo(guild.getIdLong());
								si.addXP(userId, xpToGive); // Add xp
							}
							return; // Don't do anything else with xp
						}
					}
					
					if (!cooldowns.contains(userId)) { // If user isn't on cooldown
						ServerInfo si = ServerInfo.getServerInfo(guild.getIdLong());
						
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
						int random = xpGain.get(guild)+new Random().nextInt(xpRandomGain.get(guild));
						List<String> badRoles = new ArrayList<String>();
						if (XPUtil.getLevel(currentXP) < XPUtil.getLevel(currentXP + random)) {
							// Send message if user has leveled up
							String levelUpMessage = (mentionUserOnLevelUp.get(guild) ? event.getAuthor().getAsMention() : event.getAuthor().getName())
									+ " is now Level " + XPUtil.getLevel(currentXP + random);
							if (levelUpMessagesChannel.get(guild) != 0) { // If levelup channel is set
								// Try to get the channel
								try {
									TextChannel channel = guild.getTextChannelById(levelUpMessagesChannel.get(guild));
									if (channel != null) {
										try {
											channel.sendMessage(levelUpMessage).queue();
										} catch (Exception e) {
											levelUpMessagesChannel.restore(guild, true);
										}
									} else if (levelUpMessagesChannel.get(guild) == 1) {
										final String newlevelUpMessage = "You're now Level " + XPUtil.getLevel(currentXP + random) + " in " + event.getGuild().getName() + "!";
										event.getAuthor().openPrivateChannel().queue(dm -> {
											dm.sendMessage(newlevelUpMessage).queue();
										});
									} else {
										levelUpMessagesChannel.restore(guild, true);
										event.getChannel().sendMessage(levelUpMessage).queue();
									}
								} catch (Exception e) {
									levelUpMessagesChannel.restore(guild, true);
									event.getChannel().sendMessage(levelUpMessage).queue();
								}
							} else {
								event.getChannel().sendMessage(levelUpMessage).queue();
							}
							
							
							// Give roles
							rolesDoc.forEach((level, roleId) -> {
								if (XPUtil.getLevel(currentXP + random) >= Integer.parseInt(level) && !event.getMember().getRoles().contains(roleId)) {
									if (guild.getRoleById((String) roleId) != null) {
										try {
											guild.addRoleToMember(userId, guild.getRoleById((String) roleId)).queue();
										} catch (Exception e) {
											String roleName = guild.getRoleById((String) roleId).getName();
											SharkUtil.sendOwner(guild, "Hello! I don't seem to have permissions " +
												"to add " + roleName + " to " + event.getAuthor().getAsTag() + ". My role might " +
												"not be above " + roleName + " in the hierarchy, or I don't have enough permission.");
										}
									} else {
										badRoles.add(level);
										SharkUtil.sendOwner(guild, "Hello! I was unable to find a role " +
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
								&& !leaderboardCooldowns.contains(guild.getIdLong())) {
							
							// Check for missing channel
							if (guild.getTextChannelById(sdbLeaderboard.get("channel", (long)0)) == null) {
								SharkUtil.sendOwner(guild, 
					        			"Hey! I wasn't able to find the leaderboard message's channel, so I've reset it. " + 
					        			"Make sure to set it again if this wasn't intentional.");
								si.updateModuleDocument(ModuleType.XP.getTechName(), sdbLeaderboard.remove("message").remove("channel").build());
								return;
							}
							
							// Check for missing message
							if (guild.getTextChannelById(sdbLeaderboard.get("channel", (long)0))
									 .retrieveMessageById(sdbLeaderboard.get("message", (long)0)) == null) {
								SharkUtil.sendOwner(guild, 
					        			"Hey! I wasn't able to find the leaderboard message, so I've reset it. " + 
					        			"Make sure to set it again if this wasn't intentional.");
								si.updateModuleDocument(ModuleType.XP.getTechName(), sdbLeaderboard.remove("message").remove("channel").build());
								return;
							}
							
							// Attempt to update leaderboard
							guild
								.getTextChannelById(sdbLeaderboard.get("channel", (long)0))
								.retrieveMessageById(sdbLeaderboard.get("message", (long)0))
								.queue(message -> {
									long guildId = message.getGuild().getIdLong();
									try {
										message.editMessage(XPUtil.getLeaderboard(message.getGuild(), 1, xpDoc).setTimestamp(Instant.now()).build()).queue();
									} catch (Exception e) {
										SharkUtil.sendOwner(guild, 
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

	ChannelIDSetting levelUpMessagesChannel; 
	BooleanSetting mentionUserOnLevelUp; 
	public BooleanSetting showServerInRankCard; 
	PositiveIntegerSetting xpGain;
	PositiveIntegerSetting xpRandomGain;
	public PositiveIntegerSetting xpLimit;
	@Override
	public void setupSettings() {
		levelUpMessagesChannel = new ChannelIDSetting(this, "levelUpMessagesChannel", 0, true);
		mentionUserOnLevelUp = new BooleanSetting(this, "mentionUserOnLevelUp", false, true);
		showServerInRankCard = new BooleanSetting(this, "showServerInRankCard", true, true);
		xpGain = new PositiveIntegerSetting(this, "xpGain", 20, true);
		xpRandomGain = new PositiveIntegerSetting(this, "xpRandomGain", 5, true);
		xpLimit = new PositiveIntegerSetting(this, "xpLimit", Integer.MAX_VALUE, true);
		
		this.addSetting(levelUpMessagesChannel);
		this.addSetting(mentionUserOnLevelUp);
		this.addSetting(showServerInRankCard);
		this.addSetting(xpGain);
		this.addSetting(xpRandomGain);
		this.addSetting(xpLimit);
	}
}
