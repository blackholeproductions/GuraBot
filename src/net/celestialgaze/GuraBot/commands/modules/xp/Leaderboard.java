package net.celestialgaze.GuraBot.commands.modules.xp;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class Leaderboard extends Command {

	public Leaderboard() {
		super(new CommandOptions()
				.setName("leaderboard")
				.setDescription("Gets the XP leaderboard for this server")
				.setUsablePrivate(false)
				.setCategory("XP")
				.setCooldown(5)
				.verify());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		int page = 1;
		boolean bots = false;
		// if page specified, try to set page
		if (args.length == 1) {
			try {
			    page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				SharkUtil.error(message, args[0] + " is not a valid page number");
			}
		}
		if (modifiers.length == 1) {
			if (modifiers[0].equalsIgnoreCase("show-bots")) {
				bots = true;
			}
		}
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		// workaround
		final int pageSize = 10;
		final boolean botsFinal = Boolean.valueOf(bots);
		final int pageFinal = Integer.valueOf(page);
		message.getChannel().sendMessage(new EmbedBuilder().setTitle("Waiting...").build()).queue(response -> {
			PageMessage pm = new PageMessage(response, message.getAuthor().getIdLong(), new ArgRunnable<Integer>(pageFinal) {

				@Override
				public void run() {
					response.editMessage(XPUtil.getLeaderboard(message.getGuild(), getArg(), xpDoc, botsFinal).build()).queue();
				}
				
			}, 1, getMaxSize(si.getXpMap(xpDoc).size(), pageSize));
			if (!botsFinal) {
				List<Integer> list = new ArrayList<Integer>();
				si.getXpMap(xpDoc).values()
					.stream()
					.filter((id) -> {
						Member member = message.getGuild().getMemberById(id);
						if (member != null && member.getUser().isBot()) {
							return true;
						}
						return false;
					}).forEach((id) -> {
						list.add(id);
					});
				pm.setMaxSize(getMaxSize(list.size(), pageSize));
			}
			pm.update();
		});
	}
	private int getMaxSize(int size, int pageSize) {
		return Math.toIntExact(Math.round(Math.ceil(((size+0.0)/(pageSize+0.0)))));
	}
}
