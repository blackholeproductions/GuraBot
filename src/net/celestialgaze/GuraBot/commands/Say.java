package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Message;

public class Say extends Command {

	public Say() {
		super(new CommandOptions()
				.setName("say")
				.setDescription("Make the bot say something")
				.setCooldown(0.0)
				.setCategory("Fun")
				.setUsage("<string>")
				.setNeedAdmin(true)
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		message.delete().queue();
		message.getChannel().sendMessage(SharkUtil.toString(args, " ")).queue();
	}

}
