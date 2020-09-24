package net.celestialgaze.GuraBot;

import org.json.simple.JSONObject;

public class ServerInfo {
	public static ServerInfo getServerInfo(long id) {
		return new ServerInfo(id);
	}
	public static String getFilename(long id) {
		return System.getProperty("user.dir") + "\\data\\server\\" + id + ".json";
	}
	long id;
	
	public ServerInfo(long id) {
		this.id = id;
	}
	
	public void setPrefix(String newPrefix) {
		// creating JSONObject 
        JSONObject jo = JSON.readFile(getFilename(id));
          
        // putting data to JSONObject 
        jo.put("prefix", newPrefix);
        
        JSON.writeToFile(jo, getFilename(id));
	}
	public String getPrefix() {
		JSONObject jo = JSON.readFile(getFilename(id));
		
		return (String) jo.getOrDefault("prefix", "a!	");
	}
}
