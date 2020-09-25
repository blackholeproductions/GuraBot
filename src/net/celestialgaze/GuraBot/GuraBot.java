	package net.celestialgaze.GuraBot;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.security.auth.login.LoginException;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.CommandInterpreter;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.json.BotInfo;
import net.celestialgaze.GuraBot.json.JSON;
import net.celestialgaze.GuraBot.json.StatType;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuraBot extends ListenerAdapter {
	public static JDA jda;
	public static final String DATA_FOLDER = System.getProperty("user.dir") + "\\data\\";
	public static final Color DEFAULT_COLOR = new Color(179, 217, 255);
	public static void main(String[] args) {
		System.out.println("Main function");
		try {
			JSONObject jo = JSON.readFile(System.getProperty("user.dir") + "\\data\\bot\\settings.json");
			jda = JDABuilder.createDefault((String) jo.get("token"))
					.setActivity(Activity.playing("a"))
					.build();
			jda.addEventListener(new GuraBot());
		} catch (LoginException e) {
			e.printStackTrace();
		}
		Commands.init();
		BotInfo.addLongStat(StatType.STARTS);
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
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
				BotInfo.addLongStat(StatType.COMMANDS_RUN);
				System.out.println("Successfully executed " + 
						message.getContentRaw().split(" ")[0].substring(CommandInterpreter.getPrefix(message).length())
						+ " command");
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace();
			e.printStackTrace(pw);
			String error = "Something went horribly wrong...\n" + sw.toString().substring(0, Integer.min(700, sw.toString().length())) + 
			"... \nFull message: " + message.getContentRaw().substring(0, Integer.min(message.getContentRaw().length(), 100));
			SharkUtil.error(message, error);
			BotInfo.addLongStat(StatType.ERRORS);
			message.getChannel().sendMessage("Reporting to cel...").queue(response -> {
				GuraBot.jda.getUserByTag("celestialgaze", "0001").openPrivateChannel().queue(channel -> {
					channel.sendMessage("fix ur bot").queue(crashMsg -> {
						SharkUtil.error(crashMsg, error);
						response.editMessage("Successfully reported to cel.").queue();
					});
				});
			});
		}
	}
}
