package net.celestialgaze.GuraBot.commands.classes;

import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public interface IPageCommand {
	public static final int PAGE_SIZE = 10;
	public EmbedBuilder createEmbed(Message userMessage, Message messageToEdit, int page);
	public EmbedBuilder createEmbed(Message userMessage, Message messageToEdit, int page, List<Command> extra);
}
