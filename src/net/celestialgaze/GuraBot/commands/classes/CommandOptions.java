package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class CommandOptions {
	protected String name;
	protected String description;
	protected String usage = "";
	protected String category = "Default";
	protected CommandModule module = null;
	Permission permission = null;
	protected boolean usablePrivately = true;
	protected boolean needBotAdmin = false;
	protected double cooldownDuration = 0.5;
	
	public CommandOptions() {
		
	}
	
	public CommandOptions setName(String name) {
		this.name = name;
		return this;
	}
	public CommandOptions setDescription(String description) {
		this.description = description;
		return this;
	}
	public CommandOptions setUsage(String usage) {
		this.usage = usage;
		return this;
	}
	public CommandOptions setUsablePrivate(boolean bool) {
		this.usablePrivately = bool;
		return this;
	}
	public CommandOptions setPermission(Permission permission) {
		this.permission = permission;
		return this;
	}
	public CommandOptions setNeedAdmin(boolean bool) {
		this.needBotAdmin = bool;
		return this;
	}
	public CommandOptions setCooldown(double cooldown) {
		this.cooldownDuration = cooldown;
		return this;
	}
	public CommandOptions setCategory(String category) {
		this.category = category;
		return this;
	}
	public CommandOptions setModule(CommandModule module) {
		this.module = module;
		return this;
	}
	public Pair<CommandOptions, Boolean> verify() {
		return Pair.of(this, name != null && description != null);
	}
}
