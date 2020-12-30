package net.celestialgaze.GuraBot.commands.modules.typing;

import org.bson.Document;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.RunnableListener;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TypingModule extends CommandModule {
	public TypingModule(Command... commands) {
		super(ModuleType.TYPING, commands);
	}

	public RunnableListener getListener() {
		return new RunnableListener() {
			@Override
			public void run() {
				if (currentEvent instanceof MessageReceivedEvent) {
					MessageReceivedEvent event = (MessageReceivedEvent) currentEvent;
					
					// User is replying to typing test
					if (TypeTest.tests.containsKey(event.getAuthor().getIdLong())) {
						Message message = event.getMessage();
						long targetId = TypeTest.tests.get(event.getAuthor().getIdLong());
						String content = TypeTest.contents.get(targetId);
						long startTime = TypeTest.times.get(targetId);
						String typed = message.getContentRaw();
						int errors = TypeTest.errors(content, typed);
						
						float accuracy = (1-((errors+0f)/(content.length()+0f)))*100f;
						float adjustedWPM = ((((content.length()-(errors*5f))+0f))/5f)/((System.currentTimeMillis()-startTime+0f)/60000f);
						if (adjustedWPM < 0) adjustedWPM = 0;
						// Reply with WPM 
						message.getChannel().sendMessage(new EmbedBuilder()
								.setTitle("Typing Test Results")
								.setColor(GuraBot.DEFAULT_COLOR)
								.addField("Original", content.substring(0, SharkUtil.min(content.length(), 1024)), false)
								.addField("Typed", typed.substring(0, SharkUtil.min(typed.length(), 1024)), false)
								.addField("WPM", String.format("%.2f", adjustedWPM), true)
								.addField("Errors", ""+errors, true)
								.addField("Accuracy",String.format("%.2f", accuracy) + "%", true)
								.build()).queue();
						
						// If eligible, add to leaderboard
						if (content.split(" ").length >= 100 && accuracy >= 50) { // Test was at least 100 words, and got accuracy of above 50%
							ServerInfo si = ServerInfo.getServerInfo(event.getGuild().getIdLong());
							Document typingDoc = si.getModuleDocument(type.getTechName());
							SubDocBuilder scoresDoc = new DocBuilder(typingDoc)
									.getSubDoc("scores");
							SubDocBuilder userDoc = scoresDoc.getSubDoc(event.getAuthor().getId());
							if (scoresDoc.buildThis().containsKey(event.getAuthor().getId())) { // User already has a score
								// If it's better, replace it
								float wpm = userDoc.get("wpm", 0)/100f;
								if (wpm < adjustedWPM) {
									userDoc.put("wpm", Math.round(adjustedWPM*100f));
									userDoc.put("acc", Math.round(accuracy*100f));
									si.updateModuleDocument(type.getTechName(), userDoc.build());
								}
							} else {
								// Add the user's score
								userDoc.put("wpm", Math.round(adjustedWPM*100f));
								userDoc.put("acc", Math.round(accuracy*100f));
								si.updateModuleDocument(type.getTechName(), userDoc.build());
							}
						}
						
						// Clean
						TypeTest.tests.remove(event.getAuthor().getIdLong());
						TypeTest.contents.remove(targetId);
						TypeTest.times.remove(targetId);
					}
				}
			}
		};
	}

	@Override
	public void setupSettings() {
		// TODO Auto-generated method stub
		
	}
}
