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
	
	CallHeuristic(){
		System.out.println("Call Heuristic Prepared.");
	}
	
	protected void internalTransform(Body b, String phaseName, Map options){
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefUnitGraph(b);
		//Finds Post dominators.
		d = new MHGPostDominatorsFinder(g);
		//this is the patchingchain of unit in the body
		units = b.getUnits();
		
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				IfStmt ifStatement = (IfStmt) u1;
				//Look at the successor of the IfStmt and check its position
				System.out.println("\tIfStmt Found: " + ifStatement);
				for(Unit u2 : g.getSuccsOf(u1)){
					//d.getDominators(x) gets all the post dominators of x.
					System.out.println("\t\tSearching BBlock beginning with: " + u2);
					if(search_BBlock_for_InvokeStmt(u2)){
						System.out.println("\t\t\tPredict not taken because it has a invoke statment and it postdominates this unit.");
					}else{
						System.out.println("\t\t\tPrediction Uncertain");
					}
				}
			}catch(Exception e1){
				continue;
			}
		}
	}
	
	public boolean search_BBlock_for_InvokeStmt(Unit entry){
		Unit searching = entry;
		do{
			//System.out.println(searching); //Debug
			if(g.getSuccsOf(searching).size() > 1){
				//BBlock ends with a branch.
				//We did not find an invoke.
				return false;
			}else{
				try{
					InvokeStmt is_searching_a_invoke = (InvokeStmt) searching;
					//If this does not throw an exception, it is.
					//Check if there it postdominates. If it does, do not take this branch.
					return d.getDominators(searching).contains(entry);
				}catch(Exception e){
					//Nope, but continue.
				}
			}
			//We go down the only path available to g, since the previous if statement guaranteed that this does not have multiple possible successors
			if(searching == units.getLast()){
				//We just looked at the last one. No point continuing
				return false;
			}
			searching = g.getSuccsOf(searching).get(0);
		}while(searching != null);
		//Looking at the nonexistent node? No point anymore.
		return false;
	}
}

