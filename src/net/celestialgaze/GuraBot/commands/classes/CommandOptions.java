package net.celestialgaze.GuraBot.commands.classes;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class CommandOptions {
	protected String name;
	protected String description;
	protected String usage = "";
	protected String category = "Default";
	protected CommandModule module;
	Permission permission;
	protected boolean usablePrivately;
	protected boolean usablePrivatelySet = false;
	protected boolean needBotAdmin;
	protected boolean needBotAdminSet = false;
	protected double cooldownDuration;
	
	public CommandOptions() {
		this.name = "hahahaha_cel_forgot_to_put_a_name_for_the_command_what_an_IDIOT";
		this.description = "No description";
	}
	public CommandOptions(String name, String description) {
		this.name = name;
		this.description = description;
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
		this.usablePrivatelySet = true;
		return this;
	}
	public CommandOptions setPermission(Permission permission) {
		this.permission = permission;
		return this;
	}
	public CommandOptions setNeedAdmin(boolean bool) {
		this.needBotAdmin = bool;
		this.needBotAdminSet = true;
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
