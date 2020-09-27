package net.celestialgaze.GuraBot.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSON {
	private static void createFile(String filename) {
	    try {
	      File myObj = new File(filename);
	      myObj.getParentFile().mkdirs();
	      if (myObj.createNewFile()) {
	        System.out.println("File created: " + myObj.getName());
		    JSONObject jo = new JSONObject();
		    writeToFile(jo, filename);
	      }
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	
	public static void writeToFile(JSONObject jo, String filename) {
        PrintWriter pw;
        createFile(filename);
		try {
			pw = new PrintWriter(filename);
	        pw.write(jo.toJSONString());
	        pw.flush();
	        pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static JSONObject readFile(String filename) {
		// parsing file "JSONExample.json" 
        Object obj;
        createFile(filename);
		try {
			obj = new JSONParser().parse(new FileReader(filename));
	        JSONObject jo = (JSONObject) obj; 
	        return jo;
		} catch (FileNotFoundException e) {
			System.err.println("Attempted to read from non-existent file " + filename);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println(filename + " is an invalid JSON file!");
		}
		try {
			return (JSONObject) new JSONParser().parse("{}");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static JSONObject write(String filename, String name, Object value) {
		JSONObject jo = readFile(filename);
		jo.put(name, value);
		writeToFile(jo, filename);
		return jo;
	}
	@SuppressWarnings("unchecked")
	public static <T> T read(String filename, String name) {
		JSONObject jo = readFile(filename);
		return (T) jo.get(name);
	}
	@SuppressWarnings("unchecked")
	public static <T> T read(String filename, String name, T def) {
		JSONObject jo = readFile(filename);
		return (T) jo.getOrDefault(name, def);
	}
	public static JSONObjectBuilder getBuilder(String filename) {
		JSONObject jo = readFile(filename);
		return new JSONObjectBuilder(jo);
	}
	public static JSONObjectBuilder getBuilder(JSONObject jo) {
		return new JSONObjectBuilder(jo);
	}
	@SuppressWarnings("unchecked")
	public static <T> T get(JSONObject jo, String value) {
		return (T) jo.get(value);
	}
	@SuppressWarnings("unchecked")
	public static <T> T getOrDefault(JSONObject jo, String value, T def) {
		return (T) jo.getOrDefault(value, def);
	}
}
