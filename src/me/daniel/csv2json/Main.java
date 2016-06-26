package me.daniel.csv2json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//http://jsonlint.com/ - used to check the output of this program from a valid CSV
public class Main {
	
	//The index (from 0) of the value in the CSV
	private static Map<Integer, String> definedVals = new HashMap<>();
	//Contains all lines of the file.
	private static List<String> content = new ArrayList<>();
	
	private static List<JsonObject> objects = new ArrayList<>();
	
	public static void main(String[] args) {
		String filename = null;
		System.err.println("csv2json by Daniel Shaw, June 2016.");
		
		if(args.length < 1) { //The user has not passed the file path as an argument.
			System.err.println("Usage: csv2json <csv file>");
			return;
		} else { //The user has passed the file path as an argument.
			filename = args[0].trim();
		}
		
		File csvfile = new File(filename); //Checks to see if the file exists.
		if(!csvfile.exists() || csvfile.isDirectory()) {
			System.err.println("Invalid file.");
			return;
		}
		
		if(!readFile(csvfile)) return; //Reads the CSV file into memory
		
		if(!parseColumns(content.get(0))) { //Insert all the possible key values in order from index 0 to n
			System.err.println("Invalid CSV file."); //If this runs, then the file passed is an invalid CSV.
			return;
		}
		
//		definedVals.forEach((k,v) -> { //test to see if all values are in
//			System.out.println(k + " -> " + v);
//		});
		
		//Start at 1 to skip the column declaration line
		for(int i = 1; i < content.size(); i++) { //This parses all the objects out of the CSV file
			JsonObject obj = new JsonObject();
			String[] current = content.get(i).trim().split(",");
			for(int index : definedVals.keySet()) {
				//index works for definedVals.get(int) and current[int]
				String value = current[index].trim();
				obj.putValue(definedVals.get(index), value);
			}
			objects.add(obj);
		}
		
		//Now we finally output the valid JSON representation of the CSV.
		System.out.print("[");
		for(int i = 0; i < objects.size(); i++) {
			System.out.print(objects.get(i));
			if(i < objects.size()-1) {
				System.out.print(", ");
			}
		}
		System.out.println("]");
		
	}
	
	private static boolean parseColumns(String list) {
		if(!list.contains(",") || list.length() == 0) return false;
		int index = 0;
		for(String val : list.split(",")) definedVals.put(index++, val.trim());
		return true;
	}
	
	private static boolean readFile(File f) {
		try(Scanner scanner = new Scanner(new FileReader(f))) {
			while(scanner.hasNext()) {
				content.add(scanner.nextLine());
			}
		} catch(IOException e) {
			System.err.println("An error has occurred: ");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}