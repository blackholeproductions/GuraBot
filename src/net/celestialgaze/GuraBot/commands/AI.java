package net.celestialgaze.GuraBot.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.DelayedRunnable;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;
public class AI extends Command {

	public AI() {
		super(new CommandOptions()
				.setName("ai")
				.setDescription("Gets an AI chatbot's response to your message")
				.setUsage("<message>")
				.setCategory("Fun")
				.verify());
	}
	
	public String[] failResponses = {"Barack Obama was my favorite president", "The Earth is flat", "Get in the cuck shed", "You're my one true love", "Why", "Herobrine is a furry"};
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		Document output = new Document().append("response", "g");
		if (args.length == 0) {
			message.getChannel().sendMessage("no").queue();
		}
		try {
			String url = "https://some-random-api.ml/chatbot?message=" + URLEncoder.encode(SharkUtil.toString(args, " "));
		    URL obj = new URL(url);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(con.getInputStream()));
		    String inputLine;
		    StringBuffer response = new StringBuffer();
		
		    while ((inputLine = in.readLine()) != null) {
		        response.append(inputLine);
		    }
		    in.close();
		    output = Document.parse(response.toString());
		} catch (IOException e) {
			output.put("response", failResponses[new Random().nextInt(failResponses.length-1)]);
		}
		message.getChannel().sendMessage(output.getString("response")).queue();
	}

}
