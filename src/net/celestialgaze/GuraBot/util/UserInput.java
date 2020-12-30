package net.celestialgaze.GuraBot.util;

import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.GuraBot.GuraBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class UserInput extends ListenerAdapter implements Runnable {
	public Message message;
	public String[] args;
	public String[] modifiers;
	public Map<String, Object> info;
	public boolean activated = true;
	public String response;
	
	public UserInput(Message message, String[] args, String[] modifiers) {
		this.message = message;
		this.args = args;
		this.modifiers = modifiers;
		this.info = new HashMap<String, Object>();
		init();
	}
	
	public UserInput(Message message, String[] args, String[] modifiers, Map<String, Object> info) {
		this.message = message;
		this.args = args;
		this.modifiers = modifiers;
		this.info = info;
		init();
	}
	
	private void init() {
		GuraBot.jda.addEventListener(this);
		new DelayedRunnable(new Runnable() {

			@Override
			public void run() {
				if (activated) {
					SharkUtil.error(message, "Your time to reply has expired!");
					deactivate();
				}
			}
			
		}).execute(System.currentTimeMillis()+30*1000); // Expire after 30 seconds
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().getIdLong() == message.getAuthor().getIdLong() &&
				event.getChannel().getIdLong() == message.getChannel().getIdLong()) {
			response = event.getMessage().getContentRaw();
			this.run();
			deactivate();
		}
	}
	private void deactivate() {
		GuraBot.jda.removeEventListener(this);
		activated = false;
	}
}
