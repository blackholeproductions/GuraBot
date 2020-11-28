package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.HashMap;

import javax.print.Doc;

import org.bson.Document;

import com.mongodb.client.model.Indexes;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.dv8tion.jda.api.entities.Message;

public class Benchmark extends Command {

	public Benchmark() {
		super(new CommandOptions()
				.setName("benchmark")
				.setDescription("debug")
				.setCategory("Debug")
				.setNeedAdmin(true)
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage("test").queue(response -> {
			long time = System.currentTimeMillis();
			GuraBot.db.getCollection("test").createIndex(new Document()
					.append("wpm", 1)
					.append("name", "query for wpm"));
			GuraBot.db.getCollection("test").listIndexes().forEach((doc) -> {
				//message.getChannel().sendMessage(new HashMap<String, Object>(doc.entrySet()));
			});
		});
		/*
		message.getChannel().sendMessage("Running 200,000 xp updates (100,000 add, 100,000 subtract) to simulate heavy bot usage").queue(response -> {
			long time = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				ServerInfo.getServerInfo(message.getGuild().getIdLong()).addXP(message.getAuthor().getIdLong(), 1);
			}
			for (int i = 0; i < 100000; i++) {
				ServerInfo.getServerInfo(message.getGuild().getIdLong()).addXP(message.getAuthor().getIdLong(), -1);
			}
			response.getChannel().sendMessage("Took " + (System.currentTimeMillis()-time) + "ms").queue(response2 -> {
				message.getChannel().sendMessage("Running 200,000 xp reads to simulate heavy bot usage").queue();
				long time2 = System.currentTimeMillis();
				for (int i = 0; i < 200000; i++) {
					ServerInfo.getServerInfo(message.getGuild().getIdLong()).getDocument();
				}
				message.getChannel().sendMessage("Took " + (System.currentTimeMillis()-time2) + "ms").queue();
			});
		});*/
	}

}
