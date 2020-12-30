package net.celestialgaze.GuraBot.commands.modules.moderation;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Purge extends Command {

	public Purge() {
		super(new CommandOptions("purge", "Purges the specified amount of messages from the channel")
				.setUsage("<amount>")
				.setPermission(Permission.MESSAGE_MANAGE)
				.setUsablePrivate(false)
				.setCategory("Server")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		int amount = 2;
		
		if (SharkUtil.canParseInt(SharkUtil.toString(args, " "))) {
			amount = SharkUtil.parseInt(SharkUtil.toString(args, " "));
			if (amount < 2) amount = 2;
		}
		
		if (message.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			purge(message.getChannel(), amount);
		} else {
			SharkUtil.error(message, "I need the " + Permission.MESSAGE_MANAGE.getName() + "permission");
		}
	}
	
	private void purge(MessageChannel channel, int amount) {
		if (amount < 1) return;
		if (amount < 100) {
			channel.getHistory().retrievePast(amount).queue(list -> {
				channel.purgeMessages(list);
			});
		} else {
			channel.getHistory().retrievePast(100).queue(list -> {
				channel.purgeMessages(list);
				purge(channel, amount-100);
			});
		}
	}
 
}
