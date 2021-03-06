import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import java.util.concurrent.*;

//Proper documentation of this can be found in the static data generator version of this.
public class HeuristicDatabase{
	private Hashtable<SootMethod,Hashtable<Integer,HeuristicInformation>> methodToPredictionTable;
	private int heuristic_count;
	private String[] what_heuristics_at_what_row;
	private final Object print_lock = new Object();
	
	HeuristicDatabase(int h_c){
		//Initialize everything.
		methodToPredictionTable = new Hashtable<SootMethod,Hashtable<Integer,HeuristicInformation>>();
		heuristic_count = h_c;
		what_heuristics_at_what_row = new String[heuristic_count];
	}
	
	public void name_heuristic(int heuristic_num, String h_name){
		//Give names to the array for later purposes.
		if(what_heuristics_at_what_row[heuristic_num] == null){
			what_heuristics_at_what_row[heuristic_num] = h_name;
		}
	
	}
	
	public void add(SootMethod s_m, int heuristic_num, boolean taken, IfStmt the_if){
		if(!methodToPredictionTable.containsKey(s_m)){
			methodToPredictionTable.put(s_m,new Hashtable<Integer,HeuristicInformation>());
		}
		
		if(!methodToPredictionTable.get(s_m).containsKey(the_if.get_BCI())){
			methodToPredictionTable.get(s_m).put(the_if.get_BCI(),new HeuristicInformation(heuristic_count));
		}
		
		if(taken){
			methodToPredictionTable.get(s_m).get(the_if.get_BCI()).setTaken(heuristic_num,2);
		}else{
			methodToPredictionTable.get(s_m).get(the_if.get_BCI()).setTaken(heuristic_num,1);
		}
	}
	
	public void print(){
		System.out.println("[ToIB] Table of Information Beginning");
		synchronized(print_lock){
			System.out.println(heuristic_count);
			for(int a = 0; a < heuristic_count; a++){
				System.out.println(what_heuristics_at_what_row[a]);
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
				
				fixThis += "(";
				for(Type a : s_m.getParameterTypes()){
					//System.out.println(a + "\t" + a.getClass().getName());
					fixThis += typeAppend(a);
				}
				fixThis += ")" + typeAppend(s_m.getReturnType());
				
				
				
				System.out.println("Method: " + fixThis);
				System.out.println("Method Continues for " +  methodToPredictionTable.get(s_m).size());
				for(Integer a : methodToPredictionTable.get(s_m).keySet()){
					System.out.print(a + "\t");
					for(int x = 0; x < heuristic_count; x++){
						System.out.print(methodToPredictionTable.get(s_m).get(a).getTaken(x) + "\t");
					}
					System.out.println();
				}
			}
		}
		System.out.println("[ToIE] Table of Information Ending");
		
	}
	
	public HeuristicInformation getHeuristicInformation(SootMethod m, int bci){
		return methodToPredictionTable.get(m).get(bci);
	}
	public int getHeuristicCount(){
		return heuristic_count;
	}
	public String getHeuristicName(int where){
		return what_heuristics_at_what_row[where];
	}

	public String typeAppend(Type a){
		if(a instanceof IntType){
			return "I";
		}else if(a instanceof BooleanType){
			return "Z";
		}else if(a instanceof CharType){
			return "C";
		}else if(a instanceof LongType){
			return "J";
		}else if(a instanceof DoubleType){
			return "D";
		}else if(a instanceof ByteType){
			return "B";
		}else if(a instanceof FloatType){
			return "B";
		}else if(a instanceof ShortType){
			return "S"; //Unsure if this works
		}else if(a instanceof ArrayType){
			String returnme = "";
			for(int x = 0; x < ((ArrayType)a).numDimensions; x++){
				returnme += "[";
			}
			return returnme + typeAppend(((ArrayType)a).baseType);
		}else if(a instanceof VoidType){	
			return "V";
		}else if(a instanceof RefType){	
			return "L" + a.toString() + ';';
		}else{
			return "!@#ERROR#@!";
		}
	}
}

