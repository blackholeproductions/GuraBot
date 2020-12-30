package net.celestialgaze.GuraBot.commands.classes.settings;

import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandModuleSetting;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class ChannelIDSetting extends CommandModuleSetting<Long> {

	public ChannelIDSetting(CommandModule module, String name, long defaultValue, boolean editable) {
		super(module, name, defaultValue, editable);
	}

	@Override
	public boolean validate(Guild guild, Long newValue) {
		return guild.getTextChannelById(newValue) != null || newValue == 0 || newValue == 1;
	}

	@Override
	protected String getInvalidMessage(Guild guild, Long invalidValue) {
		return invalidValue + " is not the ID of a text channel within " + guild.getName() + ", so I've reset the value of " + 
				name + " in the `" + module.getName() + "` module";
	}

	@Override
	protected String display(Guild guild, Long value) {
		return (value == 0 ? "`None selected`" : (value == 1 ? "`DM`" : "<#"+value+">"));
	}

	@Override
	public boolean trySet(Guild guild, String input) {
		TextChannel channel = guild.getTextChannelById(SharkUtil.getIdFromMention(input));
		if (channel != null) {
			this.set(guild, channel.getIdLong());
			return true;
		} else if (input.equalsIgnoreCase("dm")) {
			this.set(guild, (long)1);
			return true;
		} else if (input.equalsIgnoreCase("none")) {
			this.set(guild, (long)0);
			return true;
		} else if (input.equalsIgnoreCase("0")) {
			this.set(guild, (long)0);
			return true;
		} 
		return false;
	}

	@Override
	public String getInvalidInputMessage(Guild guild, String invalidInput) {
		return "Input must be a mention or an ID of an existing text channel, `dm` for the user's DM channel, or `none`/`0` for no channel";
	}

}
