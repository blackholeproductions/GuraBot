package net.celestialgaze.GuraBot.util;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

public class InteractableMessage {
	public static Map<Long, InteractableMessage> list = new HashMap<Long, InteractableMessage>();
	
	public Map<Emote, Runnable> emoteFunctions = new HashMap<Emote, Runnable>();
	public Map<String, Runnable> emojiFunctions = new HashMap<String, Runnable>();
	
	Message message;
	public InteractableMessage(Message message) {
		this.message = message;
		list.put(message.getIdLong(), this);
	}
	public InteractableMessage addButton(Emote emote, Runnable runnable) {
		message.addReaction(emote).queue();
		emoteFunctions.put(emote, runnable);
		return this;
	}
	public InteractableMessage addButton(String emoji, Runnable runnable) {
		message.addReaction(emoji).queue();
		emojiFunctions.put(emoji, runnable);
		return this;
	}
	public Message getMessage() {
		return message;
	}
	
}
