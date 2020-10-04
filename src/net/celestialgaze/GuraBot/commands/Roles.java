package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.ArgRunnable;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class Roles extends Command {

	public Roles() {
		super(new CommandOptions()
				.setName("roles")
				.setCategory("Utility")
				.setDescription("Gets a list of the roles in this server")
				.setUsablePrivate(false)
				.setCooldown(5.0)
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		final int pageSize = 20;
		Guild guild = message.getGuild();
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl())
				.setTitle("Roles")
				.setColor(GuraBot.DEFAULT_COLOR);
		message.getChannel().sendMessage(SharkUtil.WAITING_EMBED).queue(response -> {
			new PageMessage(response, message.getAuthor().getIdLong(), new ArgRunnable<Integer>() {
				@Override
				public void run() {
					eb.setTitle("Roles (Page " + getArg() + "/" + getMaxSize(message.getGuild(), pageSize) + ")");
					eb.setDescription(getRoles(message.getGuild(), getArg(), pageSize));
					response.editMessage(eb.build()).queue();
				}
			}, 1, getMaxSize(message.getGuild(), pageSize)).update();
		});
	}
	private String getRoles(Guild guild, int page, int pageSize) {
		String rolesString = "";
		for (int i = 0; i < guild.getRoles().size(); i++) {
			if (i < (page-1)*pageSize) continue;
			if (i > page*pageSize) break;
			Role role = guild.getRoles().get(i);
			rolesString += role.getAsMention() + ": " + role.getId() + "\n";
		}
		return rolesString;
	}
	private int getMaxSize(Guild guild, int pageSize) {
		return Math.toIntExact(Math.round(Math.ceil((guild.getRoles().size()+0.0)/(pageSize+0.0))));
	}

}
