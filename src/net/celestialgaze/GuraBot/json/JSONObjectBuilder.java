package net.celestialgaze.GuraBot.json;

import org.json.simple.JSONObject;

public class JSONObjectBuilder {
	JSONObject jo;
	public JSONObjectBuilder(JSONObject jo) {
		this.jo = jo;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObjectBuilder put(Object key, Object value) {
		jo.put(key, value);
		return this;
	}
	public JSONObject build() {
		return jo;
	}
}
