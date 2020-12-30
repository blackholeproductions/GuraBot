package net.celestialgaze.GuraBot.commands.classes;

public enum ModuleType {
	CUSTOM_COMMANDS("Custom Commands", "Server", "Allows you to add custom commands", "custom_commands"),
	XP("XP", "Server", "Adds an XP system", "xp"),
	TYPING("Typing", "Typing", "Test and improve your WPM", "typing"),
	MODERATION("Moderation", "Server", "Commands to help with moderation of your server", "moderation");
	String name;
	String category;
	String description;
	String techName;
	
	ModuleType(String name, String category, String description, String techName) {
		this.name = name;
		this.category = category;
		this.description = description;
		this.techName = techName;
	}
	
	public String getModName() {
		return name;
	}
	public String getCategory() {
		return category;
	}
	public String getDescription() {
		return description;
	}
	public String getTechName() {
		return techName;
	}
}
