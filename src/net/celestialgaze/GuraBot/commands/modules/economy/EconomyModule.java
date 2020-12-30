package net.celestialgaze.GuraBot.commands.modules.economy;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.ModuleType;
import net.celestialgaze.GuraBot.commands.classes.settings.IntegerUserValueSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.LongUserValueSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.PositiveLongSetting;
import net.celestialgaze.GuraBot.commands.classes.settings.StringSetting;
import net.celestialgaze.GuraBot.util.RunnableListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EconomyModule extends CommandModule {
	public static EconomyModule instance;
	public EconomyModule(Command... commands) {
		super(ModuleType.ECONOMY, commands);
		instance = this;
	}

	@Override
	public RunnableListener getListener() {
		return new RunnableListener() {

			@Override
			public void run() {
				if (currentEvent instanceof MessageReceivedEvent) {
					MessageReceivedEvent event = (MessageReceivedEvent) currentEvent;
					
				}
			}
			
		};
	}

	public static String getCurrencyName(Guild guild) {
		return instance.currencyName.get(guild);
	}
	public static String getEffectiveCurrencyName(Guild guild, long amount) {
		return amount != 1 ? instance.pluralCurrencyName.get(guild) : instance.currencyName.get(guild);
	}
	
	public LongUserValueSetting balance;
	public StringSetting currencyName;
	public StringSetting pluralCurrencyName;
	@Override
	public void setupSettings() {
		balance = new LongUserValueSetting(this, "balance", 100, false);
		currencyName = new StringSetting(this, "currencyName", "credit", true);
		pluralCurrencyName = new StringSetting(this, "pluralCurrencyName", "credits", true);
		addSetting(balance);
		addSetting(currencyName);
		addSetting(pluralCurrencyName);
	}

}
