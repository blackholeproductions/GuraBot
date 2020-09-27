package net.celestialgaze.GuraBot.json;

public class ServerInfo {
	public final String filename;
	public static ServerInfo getServerInfo(long id) {
		return new ServerInfo(id);
	}
	public static String getFilename(long id) {
		return System.getProperty("user.dir") + "\\data\\server\\" + id + ".json";
	}
	long id;
	
	public ServerInfo(long id) {
		this.id = id;
		filename = getFilename(id);
	}
	
	public void setProperty(ServerProperty property, Object value) {
		JSON.write(filename, property.toString().toLowerCase(), value);
	}
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ServerProperty property) {
		return (T) JSON.read(filename, property.toString().toLowerCase());
	}
	public <T> T getProperty(ServerProperty property, T def) {
		return (T) JSON.read(filename, property.toString().toLowerCase(), def);
	}
}
