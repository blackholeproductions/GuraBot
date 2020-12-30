package net.celestialgaze.GuraBot.commands.modules.counting;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.settings.ChannelIDSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.PositiveIntegerSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.UserIDSetting;
import net.celestialgaze.GuraBot.util.RunnableListener;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CountingModule extends CommandModule {
	public static CountingModule instance;
	public CountingModule(Command... commands) {
		super(ModuleType.COUNTING, commands);
		instance = this;
	}

	@Override
	public RunnableListener getListener() {
		// TODO Auto-generated method stub
		return new RunnableListener() {

			@Override
			public void run() {
				if (currentEvent instanceof MessageReceivedEvent) {
					MessageReceivedEvent event = (MessageReceivedEvent) currentEvent;
					if (event.getChannelType() != ChannelType.TEXT) return;
					Guild guild = event.getGuild();
					
					if (isValidCount(event.getMessage())) {
						current.set(guild, current.get(guild)+1);
						currentUser.set(guild, event.getMessage().getMember().getIdLong());
					} else {
						if (isInChannel(event.getMessage()) && guild.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) event.getMessage().delete().queue();
					}
				}
			}
			
		};
	}

	public ChannelIDSetting channel;
	PositiveIntegerSetting current;
	public PositiveIntegerSetting xpAwarded;
	UserIDSetting currentUser;
	
	public boolean isInChannel(Message message) {
		return message.getChannel().getIdLong() == channel.get(message.getGuild());
	}
	
	public boolean isValidCount(Message message) {
		if (message.getChannel().getIdLong() == channel.get(message.getGuild()) && 
				message.getMember().getIdLong() != currentUser.get(message.getGuild()) && 
				SharkUtil.canParseInt(message.getContentRaw()) && 
				current.get(message.getGuild()) == SharkUtil.parseInt(message.getContentRaw())-1) {
			return true;
		}
		return false;
	}
	@Override
	public void setupSettings() {
		channel = new ChannelIDSetting(this, "channel", 0, true);
		current = new PositiveIntegerSetting(this, "current", 0, false);
		xpAwarded = new PositiveIntegerSetting(this, "xpAwarded", 0, true);
		currentUser = new UserIDSetting(this, "currentUser", 0, false);
		addSetting(channel);
		addSetting(current);
		addSetting(xpAwarded);
		addSetting(currentUser);
	}

}
