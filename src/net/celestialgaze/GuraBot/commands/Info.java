package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class Info extends Command {

	public Info() {
		super(new CommandOptions()
				.setName("info")
				.setDescription("Gives a basic rundown of how to utilize me")
				.setCooldown(5)
				.setCategory("Bot Info")
				.verify());
	}

	@Override
	public void init() {}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		String prefix = "";
		if (message.getChannelType().equals(ChannelType.TEXT)) {
			prefix = ServerInfo.getServerInfo(message.getGuild().getIdLong()).getPrefix();
		} else {
			prefix = Commands.defaultPrefix;
		}
		SharkUtil.info(message, 
				"Hi! I'm GuraBot. For more information about me specifically, or for an invite link, use " + prefix + "about.\n\n" +
				"The main functionalities of this bot are within 'modules.' Modules are essentially just categories of commands " +
				"that you can enable and disable for your server at will. Once you enable a module, its commands become visible in " +
				"the " + prefix + "help menu. You can see a list of these modules and how to manage them with " + prefix +"module.\n" +
				"k ill add more info later");
	}

}
