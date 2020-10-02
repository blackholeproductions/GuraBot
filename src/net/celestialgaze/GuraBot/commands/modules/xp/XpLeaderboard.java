package net.celestialgaze.GuraBot.commands.modules.xp;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class XpLeaderboard extends Command {

	public XpLeaderboard() {
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
		// if page specified, try to set page
		if (args.length == 1) {
			try {
			    page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				SharkUtil.error(message, args[0] + " is not a valid page number");
			}
		}
		final int pageFinal = Integer.valueOf(page); // workaround
		message.getChannel().sendMessage(new EmbedBuilder().setTitle("Waiting...").build()).queue(response -> {
			PageMessage pm = new PageMessage(response, message.getAuthor().getIdLong(), new ArgRunnable<Integer>(pageFinal) {

				@Override
				public void run() {
					response.editMessage(ServerInfo.getServerInfo(message.getGuild().getIdLong()).getLeaderboard(getArg()).build()).queue();
				}
				
			});
			pm.update();
		});
	}

}