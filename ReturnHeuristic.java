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
	
	ReturnHeuristic(){
		System.out.println("Return Heuristic Prepared.");
	}
	
	protected void internalTransform(Body b, String phaseName, Map options){
		//We create a Control Flow Graph using b, the body of the SootMethod.
		BriefUnitGraph g = new BriefUnitGraph(b);
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
				
				
				boolean jump_has_return = search_BBlock_for_ReturnStmt(ifStatement.getTarget());
				boolean fall_has_return = search_BBlock_for_ReturnStmt(units.getSuccOf(u1));
				
				System.out.print("\t\tJump has return. ");
				System.out.println("Fall has return.");
				
				
			}catch(Exception e){
				//Ignore this...
				continue;
			}
		}
	}
	
	public boolean search_BBlock_for_ReturnStmt(Unit searching){
		do{
			if(searching.branches()){
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
			searching = units.getSuccOf(searching);
		}while(searching != null);
		//Looking at the last node? No point anymore.
		return false;
	}
}

