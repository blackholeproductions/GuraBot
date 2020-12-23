package net.celestialgaze.GuraBot.commands;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class TestCommand extends Command {

	public TestCommand() {
		super(new CommandOptions()
				.setName("test")
				.setDescription("hi")
				.setNeedAdmin(true)
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage("waiting...").queue(response -> {
			Random random = new Random();
			Map<Integer, Long> pearlsFound = new HashMap<Integer, Long>();
			DecimalFormat df = new DecimalFormat("#.00"); 
			long startTime = System.currentTimeMillis();
			long timesTraded = 0;
			long totalPearls = 0;
			int dreamLuck = 0;
			while (true) {
				int pearls = 0;
				for (int i = 0; i < 262; i++) {
					timesTraded++;
					if (random.nextInt(411) < 20) { // Pearl traded
						pearls++;
					}
				}
				totalPearls += pearls;
				pearlsFound.put(pearls, pearlsFound.getOrDefault(pearls, (long) 0)+1);
				if (pearls >= 42) {
					dreamLuck++;
				}
				if (timesTraded % 1000000 == 0) {
					long time = System.currentTimeMillis();
					String messageToSend = "Running a calculation (for " + SharkUtil.formatDuration(time-startTime) + ") to see how lucky Dream really was if he didn't cheat. Here's some stats: \n\n"
							+ "Amount of times traded: **" + df.format(timesTraded / 1000000000.0) + " billion** (" + df.format(totalPearls / 1000000000.0) + " billion pearl trades)\n"
							+ "Amount of times gotten luck on par with or better than Dream's: " + dreamLuck + "\n"
							+ "Traded in groups of 262 (" + df.format(timesTraded / 262.0 / 1000000000.0) + " billion groups so far), this is how many times an amount of pearls were recieved in those groups: (Dream's was **42**, meaning he got 42 pearl trades out of 262 total trades) (Average is 12) \n\n";
					
					for (Entry<Integer, Long> entry : pearlsFound.entrySet()) {
						messageToSend += "[**" + entry.getKey() + "**, " + entry.getValue() + "] ";
					}
					response.editMessage(messageToSend).queue();
				}
			}
			
			
		});
	}

}
