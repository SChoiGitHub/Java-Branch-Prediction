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
		d = new MHGPostDominatorsFinder(g);
		units = b.getUnits();
		
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				IfStmt ifStatement = (IfStmt) u1;
				//Look at the successor of the IfStmt and check its position
				System.out.println("\tIfStmt Found: " + ifStatement);
				for(Unit u2 : g.getSuccsOf(u1)){
					System.out.println("\t\tSearching BBlock beginning with: " + u2);
					if(search_BBlock_for_InvokeStmt(u2, u1)){
						System.out.println("\t\t\tPredict not taken because it has a invoke statment and it postdominates this unit.");
					}else{
						System.out.println("\t\t\tPredict taken because it either does not have a invoke statment or it does not postdominates this unit.");
					}
				}
			}catch(Exception e1){
				continue;
			}
		}
	}
	
	public boolean search_BBlock_for_InvokeStmt(Unit searching, Unit do_not_postdominate_me_or_I_will_not_take_you){
		do{
			if(g.getSuccsOf(searching).size() > 1){
				//BBlock ends with a branch.
				//We did not find an invoke.
				return false;
			}else{
				try{
					InvokeStmt is_searching_a_invoke = (InvokeStmt) searching;
					//If this does not throw an exception, it is.
					//Check if there it postdominates. If it does, do not take this branch.
					return !d.getDominators(searching).contains(do_not_postdominate_me_or_I_will_not_take_you);
				}catch(Exception e){
					//Nope, but continue.
				}
			}
			//We go down the only path available to g, since the previous if statement guaranteed that this does not have multiple possible successors
			searching = g.getSuccsOf(searching).get(0);
		}while(searching != null);
		//Looking at the last node? No point anymore.
		return false;
	}
}

