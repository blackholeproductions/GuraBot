package net.celestialgaze.GuraBot.json;

public enum StatType {
	
	ERRORS(long.class),
	COMMANDS_RUN(long.class), 
	STARTS(long.class);

	private final Class<?> returnType;
	<T> StatType(Class<T> returnType) {
		this.returnType = returnType;
	}
	public <T> Class<T> getReturnType() {
		return (Class<T>) returnType.getClass();
	}
}
