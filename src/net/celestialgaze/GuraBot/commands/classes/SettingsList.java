package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class SettingsList extends Subcommand {

	public SettingsList(Command parent) {
		super(new CommandOptions()
				.setName("list")
				.setDescription("Lists the settings you can change")
				.verify(), parent);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.getChannel().sendMessage(new EmbedBuilder().setDescription(module.getSettingsList(message.getGuild())).build()).queue();
	}

}
