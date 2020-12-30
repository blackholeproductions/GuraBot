package net.celestialgaze.GuraBot.commands.classes;

import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.UserInput;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class ConfirmationCommand extends Command {

	public ConfirmationCommand(Pair<CommandOptions, Boolean> pair) {
		super(pair);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		SharkUtil.info(message, confirmationMessage(message, args, modifiers));
		new UserInput(message, args, modifiers) {

			@Override
			public void run() {
				if ((response.contains("yes")) && response.contains("no")) {
					SharkUtil.info(message, ".................what");
				} else if (response.contains("yes")) {
					confirmed(message, args, modifiers);
				} else if (response.contains("no")) {
					SharkUtil.info(message, "Okay, cancelling..");
				} else {
					SharkUtil.info(message, "????");
				}
			}
			
		};
	}
	
	public abstract void confirmed(Message message, String[] args, String[] modifiers);
	public abstract String confirmationMessage(Message message, String[] args, String[] modifiers);

}
