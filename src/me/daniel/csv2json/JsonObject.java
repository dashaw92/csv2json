package me.daniel.csv2json;

import java.util.LinkedHashMap;

public class JsonObject {
	
	//Key : value
	private LinkedHashMap<String, String> values = new LinkedHashMap<>();
	private int count = 0; //Count of values
	
	public boolean putValue(String key, String value) {
		if(values.containsKey(key)) return false;
		values.put(key, value);
		count++;
		return true;
	}
	
	//Convert this object to a valid JSON object
	@Override
	public String toString() {
		int index = 0;
		
		String string = "{\n";
		for(String key : values.keySet()) {
			String val = values.get(key);
			key = key.replace("\"", "").trim();
			val = val.replace("\"", "").trim();
			string += "\t\"" + key + "\": \"" + val + "\"";
			if(index < count-1) {
				string += ",";
			}
			index++;
			string += "\n";
		}
		string += "}";
		return string;
	}
	
}