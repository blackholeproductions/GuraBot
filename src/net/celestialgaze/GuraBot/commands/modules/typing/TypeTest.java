package net.celestialgaze.GuraBot.commands.modules.typing;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.json.JSON;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class TypeTest extends Subcommand {

	public static Map<Long, Long> tests = new HashMap<Long, Long>(); // map of users to message ids
	public static Map<Long, String> contents = new HashMap<Long, String>(); // map of msg ids to typing test contents
	public static Map<Long, Long> times = new HashMap<Long, Long>(); // map of msg ids to start times
	public static final String[] WHITESPACES = {" ", " ", " ", " ", " ", " ", ""};
	
	public TypeTest(Command parent) {
		super(new CommandOptions()
				.setName("test")
				.setDescription("Begins a typing test")
				.setUsage("<amount of words>")
				.setCooldown(3.0)
				.verify(), parent); 
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		int amount = 100;
		if (args.length == 1) {
			try {
				amount = Integer.parseInt(args[0]);
				if (amount < 0) {
					SharkUtil.error(message, "Amount cannot be less than 0");
					return;
				}	
			} catch (Exception e) {
				SharkUtil.error(message, "Invalid input");
				return;
			}
			if (amount > 200) amount = 200;
		}
		
		// perhaps xp reward for this later
		String content = "";
		Random rn = new Random();
		JSONObject jo = JSON.readFile(GuraBot.DATA_FOLDER + "bot\\words.json");
		String[] words = ((String)jo.get("words")).split(",");
		for (int i = 0; i < amount; i++) {
			content += words[rn.nextInt(words.length-1)] + " ";
		}
		content = content.trim();
		String filtered = content.replace(" ", WHITESPACES[rn.nextInt(WHITESPACES.length-1)] + "​")
								 .replace("a", "а")
								 .replace("c", "с")
								 .replace("e", "е")
								 .replace("o", Character.toString("о𐓪".charAt(rn.nextInt(1))))
								 .replace("p", "р")
								 .replace("x", "х")
								 .replace("y", "у")
								 .replace("I", "Ι")
								 .replace("l", "I");
		final String finalContent = content;
		message.getChannel().sendMessage(filtered).queue(response -> {
			tests.put(message.getAuthor().getIdLong(), response.getIdLong());
			contents.put(response.getIdLong(), finalContent);
			times.put(response.getIdLong(), System.currentTimeMillis()+250); // reaction time
		});
		
	}
	public static int errors(String x, String y) {
	    int[][] table = new int[x.length() + 1][y.length() + 1];
	 
	    for (int i = 0; i <= x.length(); i++) {
	        for (int j = 0; j <= y.length(); j++) {
	            if (i == 0) {
	            	table[i][j] = j;
	            }
	            else if (j == 0) {
	            	table[i][j] = i;
	            }
	            else {
	            	table[i][j] = SharkUtil.min(table[i - 1][j - 1] 
	                 + (x.charAt(i - 1) == y.charAt(j - 1) ? 0 : 1), //substitution
	                 table[i - 1][j] + 1, // insertion
	                 table[i][j - 1] + 1); // deletion
	            }
	        }
	    }
	 
	    return table[x.length()][y.length()];
	}
 
}
