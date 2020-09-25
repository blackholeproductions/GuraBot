	package net.celestialgaze.GuraBot;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.CommandInterpreter;
import net.celestialgaze.GuraBot.json.JSON;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
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
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getChannelType().equals(ChannelType.PRIVATE)) { // Handle dm logging
			System.out.println("[DM " + event.getPrivateChannel().getUser().getAsMention() + "] " +
					event.getAuthor().getAsTag() + ": " +
					event.getMessage().getContentDisplay());
		} else {
			System.out.println("[" + event.getGuild().getName() + " - #" + event.getChannel().getName() + "] " +
					event.getAuthor().getAsTag() + ": " +
					event.getMessage().getContentDisplay());
		}
		// Run any commands and log when successful
		if (CommandInterpreter.readExecute(event.getMessage())) {
			System.out.println("Successfully executed " + 
					event.getMessage().getContentRaw().split(" ")[0].substring(CommandInterpreter.getPrefix(event.getMessage()).length())
					+ " command");
		}
	}
}
