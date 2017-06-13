import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.jimple.internal.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class ReturnHeuristic extends BodyTransformer {
	private PatchingChain<Unit> units;
	private BriefUnitGraph g;
	
	
	
	ReturnHeuristic(){
		System.out.println("Return Heuristic Prepared.");
	}
	
	protected void internalTransform(Body b, String phaseName, Map options){
		System.out.println("Applying " + phaseName + " on " + b.getMethod());
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefUnitGraph(b);
		//units represents all the statements within the body. Local varibles and exceptions are in other chains.
		units = b.getUnits();
		//Iterate between all Unit objects in units.
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				JIfStmt ifStatement = (JIfStmt) u1;
				//Look at the successor of the IfStmt and check its position
				System.out.println("\tIfStmt Found: " + ifStatement);
				
				
				for(Unit u2 : g.getSuccsOf(u1)){
					System.out.println("\t\tSearching BBlock beginning with: " + u2);
					if(search_BBlock_for_ReturnStmt(u2)){
						
						System.out.println("\t\t\tPredict not taken because it has a return.");
					}else{
						System.out.println("\t\t\tPredict taken because it does not have a return.");
					}
				}
				
			}catch(Exception e1){
				/*
				try{
					//Maybe its an switch statement?
					SwitchStmt switchStmt = (SwitchStmt) u1;
					
					System.out.println("\tSwitchStmt Found: " + switchStmt);
					
					for(Unit u2 : g.getSuccsOf(u1)){
						System.out.println("\t\tSearching BBlock beginning with: " + u2);
						if(search_BBlock_for_ReturnStmt(u2)){
							System.out.println("\t\t\tPredict not taken because it has a return.");
						}else{
							System.out.println("\t\t\tPredict taken because it does not have a return.");
						}
					}
				}catch(Exception e2){
					
				}
				*/
				//Ignore this...
				continue;
			}
		}
	}
	
	public boolean search_BBlock_for_ReturnStmt(Unit searching){
		do{
			//System.out.println(searching); //Debugs
			if(g.getSuccsOf(searching).size() > 1){
				//BBlock ends with a branch.
				//We did not find a return.
				return false;
			}else{
				try{
					ReturnStmt is_searching_a_return_statment = (ReturnStmt) searching;
					return true; //If this does not throw an exception, it is.
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

