import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class CallHeuristic extends BodyTransformer {
	private PatchingChain<Unit> units;
	private BriefUnitGraph g;
	private MHGPostDominatorsFinder<Unit> d;
	private FileOutputStream file;
	
	CallHeuristic(){
		try{
			file = new FileOutputStream("CallHeuristicAnalysis", false);
		}catch(Exception e){
			
		}
		System.out.println("Call Heuristic Prepared.");
	}
	
	public void printAndWriteToFile(String s){
		//System.out.println(s);
		try{
			file.write(s.getBytes());
			file.write('\n');
		}catch(Exception e){
			
		}
	}
	
	
	protected void internalTransform(Body b, String phaseName, Map options){
		try{
			file = new FileOutputStream("Soot_Heuristic_Information/call_h_" + b.getMethod(), false);
		}catch(Exception e){
			
		}	
		
		
		printAndWriteToFile("Applying " + phaseName + " on " + b.getMethod());
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefUnitGraph(b);
		//Finds Post dominators.
		d = new MHGPostDominatorsFinder<Unit>(g);
		//this is the patchingchain of unit in the body
		units = b.getUnits();
		
		for(Unit u1 : units){
			
			/*
			try{
				InvokeStmt is_searching_a_invoke = (InvokeStmt) u1;
				printAndWriteToFile("INVOKE!  " + is_searching_a_invoke); //Debug to find invokes.
			}catch(Exception e){
				//Nope, but continue.
			}
			*/
			
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				IfStmt ifStatement = (IfStmt) u1;
				
				if(search_BBlock_for_InvokeStmt(ifStatement.getTarget())){
					printAndWriteToFile("\tIfStmt Found: " + ifStatement);
					printAndWriteToFile("\t\tGoto Destination beginning with: " + ifStatement.getTarget());
					printAndWriteToFile("\t\t\tPredict not taken because it has a invoke statment and it postdominates this unit.");
				}else{
					//printAndWriteToFile("\t\t\tPrediction Uncertain");
				}
			}catch(Exception e1){
				continue;
			}
		}
		try{
			file.close();
		}catch(Exception e){
			
		}
	}
	
	public boolean search_BBlock_for_InvokeStmt(Unit entry){
		Unit searching = entry;
		do{
			try{
				InvokeStmt is_searching_a_invoke = (InvokeStmt) searching;
				//If this does not throw an exception, it is.
				//Check if there it postdominates. If it does, do not take this branch.
				return d.getDominators(searching).contains(entry);
			}catch(Exception e){
				//Nope, but continue.
			}
			
			if(g.getSuccsOf(searching).size() == 1){
				//We go down the only path available to g, since the previous if statement guaranteed that this does not have multiple possible successors
				searching = g.getSuccsOf(searching).get(0);
			}else{
				//Two possibilities:
				//BBlock ends with a branch. The BBlock ended without finding an invoke.
				//OR
				//No more successors, nothing to search.
				return false;
			}
		}while(searching != null);
		//Looking at the nonexistent node? No point anymore.
		return false;
	}
}

