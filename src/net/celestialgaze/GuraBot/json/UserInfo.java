
package net.celestialgaze.GuraBot.json;

import net.celestialgaze.GuraBot.GuraBot;

public class UserInfo {
	public final String filename;
	public static UserInfo getUserInfo(long id) {
		return new UserInfo(id);
	}
	public static String getFilename(long id) {
		return GuraBot.DATA_FOLDER + "user\\" + id + ".json";
	}
	long id;
	
	public UserInfo(long id) {
		this.id = id;
		filename = getFilename(id);
	}
	
	public void setProperty(UserProperty property, Object value) {
		JSON.write(filename, property.toString().toLowerCase(), value);
	}
	@SuppressWarnings("unchecked")
	public <T> T getProperty(UserProperty property) {
		return (T) JSON.read(filename, property.toString().toLowerCase());
	}
	public <T> T getProperty(UserProperty property, T def) {
		return (T) JSON.read(filename, property.toString().toLowerCase(), def);
	}
}
