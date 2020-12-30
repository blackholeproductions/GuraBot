package net.celestialgaze.GuraBot.commands.modules.moderation;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.util.RunnableListener;

public class ModerationModule extends CommandModule {

	public ModerationModule(Command[] commands) {
		super(ModuleType.MODERATION, commands);
	}

	@Override
	public RunnableListener getListener() {
		// TODO Auto-generated method stub
		return new RunnableListener() {

			@Override
			public void run() {
				
			}
			
		};
	}

	@Override
	public void setupSettings() {
		
	}

}
