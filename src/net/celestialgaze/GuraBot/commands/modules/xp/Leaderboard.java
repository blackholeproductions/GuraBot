package net.celestialgaze.GuraBot.commands.modules.xp;

import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
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
		boolean left = false;
		boolean usernames = false;
		// if page specified, try to set page
		if (args.length == 1) {
			try {
			    page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				SharkUtil.error(message, args[0] + " is not a valid page number");
			}
		}
		for (String modifier : modifiers) {
			if (modifier.equalsIgnoreCase("show-bots")) {
				bots = true;
			} else if (modifier.equalsIgnoreCase("show-left")) {
				left = true;
			} else if (modifier.equalsIgnoreCase("usernames")) {
				usernames = true;
			}
		}
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		// workaround
		final int pageSize = 10;
		final boolean botsFinal = Boolean.valueOf(bots);
		final boolean leftFinal = Boolean.valueOf(left);
		final boolean usernamesFinal = Boolean.valueOf(usernames);
		final int pageFinal = Integer.valueOf(page);
		message.getChannel().sendMessage(new EmbedBuilder().setTitle("Waiting...").build()).queue(response -> {
			PageMessage pm = new PageMessage(response, message.getAuthor().getIdLong(), new ArgRunnable<Integer>(pageFinal) {

				@Override
				public void run() {
					response.editMessage(XPUtil.getLeaderboard(message.getGuild(), getArg(), xpDoc, botsFinal, usernamesFinal, leftFinal).build()).queue();
				}
				
			}, 1, getMaxSize(si.getXpMap(xpDoc, botsFinal, leftFinal).size(), pageSize));
			pm.update();
		});
	}
	private int getMaxSize(int size, int pageSize) {
		System.out.println(Math.toIntExact(Math.round(Math.ceil(((size-1.0)/(pageSize+0.0))))));
		System.out.println((size-0.0)/(pageSize+0.0));
		System.out.println(size);
		System.out.println(pageSize);
		
		return Math.toIntExact(Math.round(Math.ceil(((size-0.0)/(pageSize+0.0)))));
	}
}
