package net.celestialgaze.GuraBot.commands.modules.typing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.db.DocBuilder;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.db.SubDocBuilder;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class TypeLeaderboard extends Subcommand {

	public TypeLeaderboard(Command parent) {
		super(new CommandOptions()
				.setName("leaderboard")
				.setDescription("Gets the leaderboard of top typists")
				.setUsage("<page>")
				.verify(), parent);
	}
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		Document typingDoc = ServerInfo.getServerInfo(message.getGuild().getIdLong()).getModuleDocument("typing");
				
		message.getChannel().sendMessage(SharkUtil.WAITING_EMBED).queue(response -> {
			
			new PageMessage(response, message.getAuthor().getIdLong(), new ArgRunnable<Integer>() {

				@Override
				public void run() {
					response.editMessage(getLeaderboard(message.getGuild(), getArg(), 10, typingDoc).build()).queue();
				}
				
			}, 1, 1).update();
		});
	}
	
	private EmbedBuilder getLeaderboard(Guild guild, int page, int pageSize, Document typingDoc) {
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Typist Leaderboard")
				.setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl())
				.setColor(GuraBot.DEFAULT_COLOR)
				.setFooter("Page " + page);
		SubDocBuilder leaderboardDoc = new DocBuilder(typingDoc)
				.getSubDoc("scores");
		Map<Long, Float> wpmMap = new HashMap<Long, Float>();
		Map<Long, Float> accMap = new HashMap<Long, Float>();
		
		leaderboardDoc.buildThis().forEach((key, val) -> {
			long id = Long.parseLong(key);
			Document doc = (Document)val;
			int wpm = doc.getInteger("wpm", 0);
			int accuracy = doc.getInteger("acc", 0);
			wpmMap.put(id, (wpm/100f));
			accMap.put(id, (accuracy/100f));	
		});
		
		List<Long> sortedIds = new ArrayList<Long>();
		wpmMap.entrySet()
		   .stream()
		   .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		   .forEach(entry -> {
			   sortedIds.add(entry.getKey());
		   });

		String description = "";
		int i = 0;
		for (long id : sortedIds) {
			if (i == 0) {
				Member member = guild.getMemberById(id);
				if (member != null) {
					eb.setThumbnail(member.getUser().getEffectiveAvatarUrl());
				}
			}
			description += (i == 0 ? "**" : "") + (i+1) + ". <@!" + id + "> - " +
			String.format("%.2f", wpmMap.get(id)) + " WPM " + accMap.get(id) + "%" + (i == 0 ? "**" : "") + "\n";
			i++;
		}
		eb.setDescription(description);
		return eb;
	}

}
