import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//Maybe we don't need this...
public class HeuristicDatabase{
	private Hashtable<SootMethod,Vector<HeuristicIfPair>> methodToPredictionTable;
	private int heuristic_count;
	private String[] what_heuristics_at_what_row;
	
	HeuristicDatabase(int h_c){
		//Initialize everything.
		methodToPredictionTable = new Hashtable<SootMethod,Vector<HeuristicIfPair>>();
		heuristic_count = h_c;
		what_heuristics_at_what_row = new String[6];
	}
	
	public void name_heuristic(int heuristic_num, String h_name){
		//Give names to the array for later purposes.
		if(what_heuristics_at_what_row[heuristic_num] == null){
			what_heuristics_at_what_row[heuristic_num] = h_name;
		}
	}
	
	public void add(SootMethod s_m, int if_num, int heuristic_num, boolean taken, IfStmt the_if){
		//If we have the key, then ignore this, otherwise, add it to the hashtable we have.
		if(!methodToPredictionTable.containsKey(s_m)){
			System.out.println(s_m + " does not exist yet in the hashtable"); //Debug
			Vector<HeuristicIfPair> new_vector = new Vector<HeuristicIfPair>();
			new_vector.add(new HeuristicIfPair(heuristic_count,the_if));
			methodToPredictionTable.put(s_m, new_vector);
		}else if(if_num >= methodToPredictionTable.get(s_m).size()){
			//if the element at this key exists, we may need to extend it.
			System.out.println("the vector is too small at size " + methodToPredictionTable.get(s_m).size()); //Debug
			methodToPredictionTable.get(s_m).add(new HeuristicIfPair(heuristic_count,the_if));
		}
		
		//2 for taken, 1 for untaken.
		if(taken){
			System.out.println("Set " + s_m + " at if " + if_num + " to " + 2);
			methodToPredictionTable.get(s_m).get(if_num).setTaken(heuristic_num,2);
		}else{
			System.out.println("Set " + s_m + " at if " + if_num + " to " + 1);
			methodToPredictionTable.get(s_m).get(if_num).setTaken(heuristic_num,1);
		}
	}
	
	//if_num independent heuristic version of the add. (REQUIRES THAT A PREVIOUS HEURISTIC HAS USED if_num TO INITIALIZE THE VECTOR PROPERLY.
	public void add(SootMethod s_m, int heuristic_num, boolean taken, IfStmt the_if){
				
		if(!methodToPredictionTable.containsKey(s_m)){
			throw new RuntimeException("Error: this version of add requires a fully sized, but not filled Hashtable.");
		}
		
		int where_is_the_if = -1;
		for(int x = 0; x < methodToPredictionTable.get(s_m).size(); x++){
			if(methodToPredictionTable.get(s_m).get(x).getIfStmt() == the_if){
				where_is_the_if = x;
			}
		}
		
		if(where_is_the_if == -1){
			throw new RuntimeException("Error: could not find IfStmt within vector.");
		}else{
			if(taken){
				System.out.println("Set " + s_m + " at if " + where_is_the_if + " to " + 2);
				methodToPredictionTable.get(s_m).get(where_is_the_if).setTaken(heuristic_num,2);
			}else{
				System.out.println("Set " + s_m + " at if " + where_is_the_if + " to " + 1);
				methodToPredictionTable.get(s_m).get(where_is_the_if).setTaken(heuristic_num,1);
			}
		}
	}
	
	public void print(){
		System.out.println("[ToIB] Table of Information Beginning");
		System.out.println(heuristic_count);
		for(int a = 0; a < heuristic_count; a++){
			System.out.println(what_heuristics_at_what_row[a] + "\t\t(Col #" +  a + ": " + what_heuristics_at_what_row[a] + ")");
		}
		for(SootMethod s_m : methodToPredictionTable.keySet()){
			//System.out.println(methodToPredictionTable.get(s_m).size());
			
			
			//This here helps analysis easier with the data aggregator.
			String fixThis = s_m.toString();
			for(int x = 0; x < fixThis.length(); x++){
				if(fixThis.charAt(x) == ':'){
					fixThis = fixThis.substring(1, x);
					fixThis += "." + s_m.getName();
				}
			}
			
			System.out.println("Method: " + fixThis);
			System.out.println("Method Continues for " +  methodToPredictionTable.get(s_m).size());
			for(int i_n = 0; i_n < methodToPredictionTable.get(s_m).size(); i_n++){
				for(int h = 0; h < heuristic_count; h++){
					System.out.print(methodToPredictionTable.get(s_m).get(i_n).getTaken(h) + " ");
				}
				System.out.print("\t" + methodToPredictionTable.get(s_m).get(i_n).getIfStmt());
				System.out.print('\n');
			}
		}
		System.out.println("[ToIE] Table of Information Ending");
	}
}

