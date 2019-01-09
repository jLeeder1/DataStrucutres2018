package astaire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MyController implements Controller {
	//---------------------------------------------------------------------------------------------
	//Files to load in
	private String groupFile = "files/danceShowData_danceGroups.csv";
	private static String danceFile = "files/danceShowData_dances.csv";
	private String orderFile = "files/danceShowData_runningOrder.csv";
	//---------------------------------------------------------------------------------------------
	//ALL TASKS
	private static MyCSVReader reader;
	//These are used  to avoid concurrency errors and changes to the existing collections
	//stores strings to be returned by method
	private static ArrayList<String> listToReturn = new ArrayList<String>();
	//stores the values from the danceMap (this will be iterated over)
	private static ArrayList<String> tempList = new ArrayList<String>();
	//---------------------------------------------------------------------------------------------
	//TASK 1 + 2
	//Stores the dance groups, key = group name, values = dancers names
	private static HashMap<String, ArrayList<String>> groupMap;
	//Stores the dances, key = dance name, values = dancers names/group names
	private static HashMap<String, ArrayList<String>> danceMap;
	//List of group names, e.g. "juniors", "intermediates"
	private static ArrayList<String> groupKeyList;
	//---------------------------------------------------------------------------------------------
	//TASK 2 ONLY
	//This is done locally
	
	//---------------------------------------------------------------------------------------------
	//TASK 3
	//Linked list for task 3
	private static ArrayList<String> orderList;
	//Holds a completed map with just dances as keys and dancer names (no groups) as values
	private static HashMap<String, ArrayList<String>> completeMap;
	//---------------------------------------------------------------------------------------------
	
	
	/**
	 * Constructs a MyController object
	 * @throws IOException Thrown if files necessary to construct object cannot be found
	 */
	public MyController() throws IOException {
		reader = new MyCSVReader();
		groupMap = new HashMap<String, ArrayList<String>>();
		danceMap = new HashMap<String, ArrayList<String>>();
		completeMap = new HashMap<String, ArrayList<String>>();
		listToReturn = new ArrayList<String>();
		tempList = new ArrayList<String>();
		orderList = new ArrayList<String>();
		
		groupMap = reader.mapReader(groupFile);
		danceMap = reader.mapReader(danceFile);
		orderList = reader.listReader(orderFile);
		setGroupList();
		createCompleteMap();
		}
	
	public static void main(String[] args) throws IOException {
		MyController controller = new MyController();
		}
	
	/**
	 * Creates a HashMap with the keys as dancers and the values as ArrayLists of their associated dancers
	 * This differs from the dance map as completeMap contains no group names only the dancers names
	 */
	public static void createCompleteMap() {
		
		for(Entry<String, ArrayList<String>> entry : danceMap.entrySet()) {
			
			ArrayList<String> toIterate = new ArrayList<String>();
			ArrayList<String> toReturn = new ArrayList<String>();
			
			toIterate.addAll(entry.getValue());
			
			for(String str : toIterate) {
				if(groupKeyList.contains(str)) {
					toReturn.addAll(groupMap.get(str));
				}
				else {
					toReturn.add(str);
				}
			}
			completeMap.put(entry.getKey(), toReturn);
			
		}
	}
	
	
	/**
	 * Creates a List of group names from the keys in groupMap
	 */
	private void setGroupList(){
		ArrayList<String> groupKeys = new ArrayList<String>();
		for(Entry<String, ArrayList<String>> entry : groupMap.entrySet()) {
			groupKeys.add(entry.getKey().toString());
		}
		groupKeyList = groupKeys;
	}
	
	private static void clearLists() {
		listToReturn.clear();
		tempList.clear();
	}
	
		
	/**
	 * Checks feasibility of a given running order.
	 * @param filename	the name of a tab-separated CSV file containing a proposed running order
	 * @param gaps the required number of gaps between dances for each dancer
	 * @return listToReturn a String representation of potential issues
	 * @throws IOException
	 */
	@Override
	public String listAllDancersIn(String dance) {
		
		ArrayList<String> toReturn = new ArrayList<String>();
		//Check to see if the given string exists in the map
		if(completeMap.get(dance) != null){
			toReturn.addAll(completeMap.get(dance));
		}
		else{
			toReturn.add("The song: " + dance + " is not in the listing, please enter another");
		}
			return toReturn.toString();
	}

	/**
	 * Lists all dance numbers and the name of the respective performers in alphabetical order.
	 * @return sb a String representation of dance numbers 
	 * 			and the name of the respective performers in alphabetical order
	 */
	@Override
	public String listAllDancesAndPerformers() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("The list of performances and performers in alphabetical order:" + "\n");
		TreeMap<String, ArrayList<String>> sortedMap = new TreeMap<String, ArrayList<String>>();
		
		for(Entry<String, ArrayList<String>> entry : completeMap.entrySet()){
			ArrayList<String> temporaryList = new ArrayList<String>();
			temporaryList.addAll(entry.getValue());
			temporaryList.sort(null);
			sortedMap.put(entry.getKey(), temporaryList);
		}
		for(Entry<String, ArrayList<String>> entry : sortedMap.entrySet()){
			sb.append( "\n" + "\n" + entry.getKey() + "\n");
			sb.append(entry.getValue().toString() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Checks feasibility of a given running order.
	 * @param filename	the name of a tab-separated CSV file containing a proposed running order
	 * @param gaps the required number of gaps between dances for each dancer
	 * @return	a String representation of potential issues
	 * @throws IOException 
	 */
	public String checkFeasibilityOfRunningOrder(String filename, int gaps) throws IOException {
		
		StringBuilder myString = new StringBuilder();
		orderList.clear();
		String path = "files/";
		orderList.addAll(reader.listReader(path + filename));
		
		if(gaps <= 0){
			gaps=1;
		}
		//So we loop though all of the orderList
		for(int index = 0; index < orderList.size()-1; index++){
			
			ArrayList<String> currentDancers = new ArrayList<String>();
			//Get a list of current dancers from the complete map
			String keyToGet = orderList.get(index);
			currentDancers.addAll(completeMap.get(keyToGet));
			
			int counter = 1;
			boolean isDone = false;
			//So it will iterate the amount of gaps we have
			while(gaps >= counter && isDone == false){
				ArrayList<String> nextDancers = new ArrayList<String>();
				
				//make an array list to store values from the completeMap at index + counter
				// e.g. index 0 + counter 1 or index 0 plus counter 2
				int nextSongNum = index + counter;
				
				if(nextSongNum < orderList.size()){
					String nextKey = orderList.get(nextSongNum);
					nextDancers.addAll(completeMap.get(nextKey));
					
						//Returns true if the two specified collections have no elements in common.
						if(!Collections.disjoint(currentDancers, nextDancers)){//Think this will be a problem
							myString.append("\n" + "There is a problem between: " + "\n"+ orderList.get(index) + " and " + "\n"+ orderList.get(index + counter) + "\n\n");
						}
						counter++;
				}
				else{
					isDone = true;
				}
			}
		}
	return myString.toString();
	}
		

	@Override
	public String generateRunningOrder(int gaps) {
		// TODO Auto-generated method stub
		return null;
	}
}
