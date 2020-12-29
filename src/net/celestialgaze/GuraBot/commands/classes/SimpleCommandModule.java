package net.celestialgaze.GuraBot.commands.classes;

import net.celestialgaze.GuraBot.util.RunnableListener;

public class SimpleCommandModule extends CommandModule {

	public SimpleCommandModule(ModuleType type, Command...commands) {
		super(type, commands);
	}

	@Override
	public RunnableListener getListener() {
		return new RunnableListener() {

			@Override
			public void run() {
				
			}
			
		};
	}

	@Override
	public void setupSettings() {
		// TODO Auto-generated method stub
		
	}
}
