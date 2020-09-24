	package net.celestialgaze.GuraBot;

import javax.security.auth.login.LoginException;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.CommandInterpreter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuraBot extends ListenerAdapter {
	public static JDA jda;
	public static void main(String[] args) {
		System.out.println("Main function");
		try {
			jda = JDABuilder.createDefault("NzU4MzU1OTM1NjYzMDk1ODA4.X2twAA.1TeOBvlXwT25-TNRHX8u4pRHxug")
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
