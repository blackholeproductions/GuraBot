package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.internal.utils.tuple.Pair;

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
		Guild guild = message.getGuild();
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl())
				.setTitle("Roles")
				.setColor(GuraBot.DEFAULT_COLOR);
		String description = "";
		for (Role role : guild.getRoles()) {
			description += role.getAsMention() + " (" + role.getId() + ")\n";
		}
		eb.setDescription(description);
		message.getChannel().sendMessage(eb.build()).queue();
	}

}
