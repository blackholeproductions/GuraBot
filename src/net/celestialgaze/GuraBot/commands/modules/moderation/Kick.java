package net.celestialgaze.GuraBot.commands.modules.moderation;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.ConfirmationCommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class Kick extends ConfirmationCommand {

	public Kick() {
		super(new CommandOptions()
				.setName("kick")
				.setDescription("Kicks the specificed user")
				.setUsage("<user>")
				.setPermission(Permission.KICK_MEMBERS)
				.setUsablePrivate(false)
				.setCategory("Server")
				.verify());
	}

	@Override
	public void confirmed(Message message, String[] args, String[] modifiers) {
		Member member = SharkUtil.getMember(message, args, 0);
		if (member != null) {
			try {
				member.kick().queue(success -> {
					SharkUtil.success(message, "Successfully kicked `" + member.getUser().getAsTag() + "`!");
				});
			} catch (Exception e) {
				SharkUtil.error(message, "Unable to kick member. Do I have enough permission?");
				return;
			}
		} else {
			SharkUtil.error(message, "Couldn't find the member \"" + SharkUtil.toString(args, " ") + "\"");
		}
	}

	@Override
	public String confirmationMessage(Message message, String[] args, String[] modifiers) {
		return "Are you sure you wish to kick " + SharkUtil.toString(args, " ") + "?";
	}

}
