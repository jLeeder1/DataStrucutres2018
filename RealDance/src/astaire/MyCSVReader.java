package astaire;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;



public class MyCSVReader {
	
	private static HashMap<String, ArrayList<String>> dancerMap;
	
	public MyCSVReader() {
		dancerMap = new HashMap<String, ArrayList<String>>();
	}
	
	/**
	 * Reads a file into the program into a HashMap
	 * @param file The file you want to read 
	 * @return dancerMap A HashMap of Dances and the associated dancers or the Dance groups and the associated dancers
	 * @throws IOException Thrown if a file cannot be found by the program
	 */
	public HashMap<String, ArrayList<String>> mapReader(String file) throws IOException{
		dancerMap = new HashMap<String, ArrayList<String>>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//Skips the first line 
			br.readLine();
			String text = "";
			
			while((text = br.readLine()) != null) {
				
				//Creates a local arrayList to store the name of dancers
				ArrayList<String> dancers = new ArrayList<String>();
				
				//Splits the above string by comma and tab and stores the now separated strings in an array
				String[] result = text.split("\t|,");
				
				//Add the rest off the array to an ArrayList
				for(int index = 1; index < result.length; index++) {
					dancers.add(result[index].trim().toUpperCase());
				}
				//Add the name of the dance group and the dancers to the map
				dancerMap.put(result[0].trim().toUpperCase(), dancers);
			}
			br.close();
		}
		
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
			
		
		
		return dancerMap;
		
	}
	
	/**
	 * Returns an ArrayList comprised of the first word of each line of a file
	 * @param file The file you want to read into the program
	 * @return runningOrder An arrayList of the order of dances
	 * @throws IOException Thrown if the file cannot be found
	 */
	public ArrayList<String> listReader(String file) throws IOException{
		
		ArrayList<String> runningOrder = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//Skips the first line 
			br.readLine();
			String text = "";
			//Create an arrayList
			//For each 0 index of the line add to the list
			//return the list
			while((text = br.readLine()) != null) {
				
				//Splits the above string by comma and tab and stores the now separated strings in an array
				String[] result = text.split("\t|,");
				
				//Add the name of the dance group and the dancers to the map
				runningOrder.add(result[0].trim().toUpperCase());
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return runningOrder;
		
	}

}