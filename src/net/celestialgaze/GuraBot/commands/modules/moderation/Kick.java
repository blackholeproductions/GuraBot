package net.celestialgaze.GuraBot.commands.modules.moderation;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class Kick extends Command {

	public Kick() {
		super(new CommandOptions()
				.setName("kick")
				.setDescription("Kicks the specificed user")
				.setPermission(Permission.KICK_MEMBERS)
				.setUsablePrivate(false)
				.setCategory("Server")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		// TODO Auto-generated method stub
		
	}

}
