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
	
	HeuristicDatabase(int h_c){
		methodToPredictionTable = new Hashtable<SootMethod,Vector<HeuristicIfPair>>();
		heuristic_count = h_c;
	}
	
	public void add(SootMethod s_m, int if_num, int heuristic_num, boolean taken, IfStmt the_if){		
		if(!methodToPredictionTable.containsKey(s_m)){
			System.out.println(s_m + " does not exist yet in the hashtable"); //Debug
			Vector<HeuristicIfPair> new_vector = new Vector<HeuristicIfPair>();
			new_vector.add(new HeuristicIfPair(heuristic_count,the_if));
			methodToPredictionTable.put(s_m, new_vector);
		}
		
		if(if_num >= methodToPredictionTable.get(s_m).size()){
			System.out.println("the vector is too small at size " + methodToPredictionTable.get(s_m).size()); //Debug
			methodToPredictionTable.get(s_m).add(new HeuristicIfPair(heuristic_count,the_if));
		}
		
		if(taken){
			System.out.println("Set " + s_m + " at if " + if_num + " to " + 2);
			methodToPredictionTable.get(s_m).get(if_num).setTaken(heuristic_num,2);
		}else{
			System.out.println("Set " + s_m + " at if " + if_num + " to " + 1);
			methodToPredictionTable.get(s_m).get(if_num).setTaken(heuristic_num,1);
		}
	}
	
	
	public void print(){
		System.out.println("Printing Table of Information...");
		for(SootMethod s_m : methodToPredictionTable.keySet()){
			//System.out.println(methodToPredictionTable.get(s_m).size());
			System.out.println(s_m);
			for(int i_n = 0; i_n < methodToPredictionTable.get(s_m).size(); i_n++){
				System.out.print('\t');
				for(int h = 0; h < heuristic_count; h++){
					System.out.print(methodToPredictionTable.get(s_m).get(i_n).getTaken(h) + " ");
				}
				System.out.print(methodToPredictionTable.get(s_m).get(i_n).getIfStmt());
				System.out.print('\n');
			}
			
		}
	}
}

