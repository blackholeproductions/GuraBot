	package net.celestialgaze.GuraBot;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.security.auth.login.LoginException;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.CommandInterpreter;
import net.celestialgaze.GuraBot.db.BotInfo;
import net.celestialgaze.GuraBot.db.BotStat;
import net.celestialgaze.GuraBot.json.JSON;
import net.celestialgaze.GuraBot.util.InteractableMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class GuraBot extends ListenerAdapter {
	public static JDA jda;
	private static MongoClient mongoClient;
	public static MongoDatabase db;
	public static MongoCollection<Document> servers;
	public static MongoCollection<Document> users;
	public static MongoCollection<Document> bot;
	public static final String DATA_FOLDER = System.getProperty("user.dir") + "\\data\\";
	public static final String SETTINGS = DATA_FOLDER + "bot\\settings.json";
	public static final Color DEFAULT_COLOR = new Color(179, 217, 255);
	public static final String REGEX_WHITESPACE = "\\s+";
	public static SelfUser selfUser;
	public static String version = "0.0.0";
	public static Date startDate;
	public static void main(String[] args) {
		System.out.println("Main function");
		try {
			mongoClient = MongoClients.create("mongodb://localhost:27017");
			db = mongoClient.getDatabase("gura");
			servers = db.getCollection("servers");
			users = db.getCollection("users");
			bot = db.getCollection("bot");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			jda = JDABuilder.createDefault(bot.find(Filters.eq("name", "settings")).first().getString("token"))
					.setChunkingFilter(ChunkingFilter.ALL)
					.setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setActivity(Activity.playing("a"))
					.build();
			jda.addEventListener(new GuraBot());
			selfUser = jda.getSelfUser();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		version = JSON.read(SETTINGS, "version", "0.0.0");
		startDate = new Date(System.currentTimeMillis());
	}
	public void onReady(ReadyEvent event) {
		Commands.init();
		BotInfo.addIntStat(BotStat.STARTS);
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		Message message = event.getMessage();
		if (event.getChannelType().equals(ChannelType.PRIVATE)) { // Handle dm logging
			System.out.println("[DM " + event.getPrivateChannel().getUser().getAsMention() + "] " +
					event.getAuthor().getAsTag() + ": " +
					message.getContentDisplay());
		} else {
			System.out.println("[" + event.getGuild().getName() + " - #" + event.getChannel().getName() + "] " +
					event.getAuthor().getAsTag() + ": " +
					message.getContentDisplay());
		}
		// Run any commands and log when successful
		try {
			if (CommandInterpreter.readExecute(message)) {
				BotInfo.addIntStat(BotStat.COMMANDS_RUN);
				System.out.println("Successfully executed " + message.getContentRaw().split(" ")[0] + " command");
			}
		} catch (InsufficientPermissionException e) {
			SharkUtil.error(event.getChannel(), "Insufficient permission, need " + e.getPermission().getName());
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace();
			e.printStackTrace(pw);
			String error = "Something went horribly wrong...\n" + sw.toString().substring(0, Integer.min(700, sw.toString().length())) + 
			"... \nFull message: " + message.getContentRaw().substring(0, Integer.min(message.getContentRaw().length(), 100));
			SharkUtil.error(message, error);
			BotInfo.addIntStat(BotStat.ERRORS);
			message.getChannel().sendMessage("Reporting to cel...").queue(response -> {
				GuraBot.jda.getUserById(Long.parseLong("218525899535024129")).openPrivateChannel().queue(channel -> {
					channel.sendMessage("fix ur bot").queue(crashMsg -> {
						SharkUtil.error(crashMsg, error);
						response.editMessage("Successfully reported to cel.").queue();
					});
				});
			});
		}
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		try {
			ReactionEmote re = event.getReactionEmote();
			// Check if reaction was added to interactable
			if (InteractableMessage.list.containsKey(event.getMessageIdLong())) { 
				// Check if the reaction added has a function. If so, run it
				InteractableMessage iMessage = InteractableMessage.list.get(event.getMessageIdLong());
				if (iMessage.getOwnerId() != event.getUserIdLong()) return;
				if (re.isEmoji() && iMessage.emojiFunctions.containsKey(re.getEmoji())) {
					iMessage.emojiFunctions.get(re.getEmoji()).run();
				} else if (re.isEmote() && iMessage.emoteFunctions.containsKey(re.getEmote())) {
					iMessage.emoteFunctions.get(re.getEmote()).run();
				}
				// Remove reaction so they don't have to remove it themselves
				if (event.getChannelType().equals(ChannelType.TEXT) &&
						event.getGuild().getMemberById(jda.getSelfUser().getIdLong()).hasPermission(Permission.MESSAGE_MANAGE)) 
					event.getReaction().removeReaction(event.getUser()).queue();
			}
		} catch (InsufficientPermissionException e) {
			SharkUtil.error(event.getChannel(), "Insufficient permission, need " + e.getPermission().getName());
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace();
			e.printStackTrace(pw);
			String error = "Something went horribly wrong...\n" + sw.toString().substring(0, Integer.min(700, sw.toString().length()));
			SharkUtil.error(event.getChannel(), error);
			BotInfo.addIntStat(BotStat.ERRORS);
		}
	}
}
