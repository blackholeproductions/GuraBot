package net.celestialgaze.GuraBot.commands.classes;

public enum ModuleType {
	CUSTOM_COMMANDS("Custom Commands", "Server", "Allows you to add custom commands to your server");
	
	String name;
	String category;
	String description;
	
	ModuleType(String name, String category, String description) {
		this.name = name;
		this.category = category;
		this.description = description;
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
}
