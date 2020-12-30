package net.celestialgaze.GuraBot.commands.modules.moderation;

import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.ConfirmationCommand;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class Ban extends ConfirmationCommand {
	public Ban() {
		super(new CommandOptions()
				.setName("ban")
				.setDescription("Bans the specified user and purges messages from the specified days (default 0)")
				.setUsage("<user> \"reason\" <days>")
				.setPermission(Permission.BAN_MEMBERS)
				.setUsablePrivate(false)
				.setCategory("Server")
				.verify());
	}

	@Override
	public void confirmed(Message message, String[] args, String[] modifiers) {
		String[] quotes = SharkUtil.toString(args, " ").split("\"");
		String userString = "";
		String reasonString = "";
		int days = 0;
		
		if (quotes.length > 0) {
			userString = quotes[0].strip();
		}
		if (quotes.length > 2) {
			reasonString = quotes[1].strip();
			if (SharkUtil.canParseInt(quotes[2].strip())) {
				days = SharkUtil.parseInt(quotes[2].strip());
			}
		}
		
		Member member = SharkUtil.getMember(message, userString.split(" "), 0);
		
		if (member != null) {
			try {
				member.ban(days, reasonString).queue(success -> {
					SharkUtil.success(message, "Successfully banned `" + member.getUser().getAsTag() + "`!");
				});
			} catch (Exception e) {
				SharkUtil.error(message, "Unable to ban member. Do I have enough permission?");
				return;
			}
		} else {
			SharkUtil.error(message, "Couldn't find the member \"" + userString + "\"");
		}
	}

	@Override
	public String confirmationMessage(Message message, String[] args, String[] modifiers) {
		return "Are you sure you wish to ban " + SharkUtil.toString(args, " ") + "?";
	}
}
