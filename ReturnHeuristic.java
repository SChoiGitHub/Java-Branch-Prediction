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
	private FileOutputStream file;
	HeuristicDatabase h_d;
	int h_id;
	int if_num;
	
	ReturnHeuristic(HeuristicDatabase hd, int heuristic_id){
		System.out.println("Return Heuristic Prepared.");
		h_d = hd;
		h_id = heuristic_id;
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
		System.out.println("Applying " + phaseName + " on " + b.getMethod());
		h_d.name_heuristic(h_id,phaseName);
		
		try{
			file = new FileOutputStream("Soot_Heuristic_Information/return_h_" + b.getMethod(), false);
		}catch(Exception e){
			
		}
		
		
		
		printAndWriteToFile("Applying " + phaseName + " on " + b.getMethod());
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefUnitGraph(b);
		//units represents all the statements within the body. Local varibles and exceptions are in other chains.
		units = b.getUnits();
		//Iterate between all Unit objects in units.
		if_num = -1;
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				JIfStmt ifStatement = (JIfStmt) u1;
				if_num++;
				//Look at the successor of the IfStmt and check its position

				
				if(search_BBlock_for_ReturnStmt(ifStatement.getTarget())){
					printAndWriteToFile("\tIfStmt Found: " + ifStatement);
					printAndWriteToFile("\t\tGoto Destination beginning with: " + ifStatement.getTarget());
					printAndWriteToFile("\t\t\tPredict not taken because it has a return.");
					h_d.add(b.getMethod(),if_num,h_id,false,ifStatement);
				}else{
					//printAndWriteToFile("\t\t\tPrediction Uncertain.");
				}
				
			}catch(Exception e1){
				/*
				try{
					//Maybe its an switch statement?
					SwitchStmt switchStmt = (SwitchStmt) u1;
					
					printAndWriteToFile("\tSwitchStmt Found: " + switchStmt);
					
					for(Unit u2 : g.getSuccsOf(u1)){
						printAndWriteToFile("\t\tSearching BBlock beginning with: " + u2);
						if(search_BBlock_for_ReturnStmt(u2)){
							printAndWriteToFile("\t\t\tPredict not taken because it has a return.");
						}else{
							printAndWriteToFile("\t\t\tPredict taken because it does not have a return.");
						}
					}
				}catch(Exception e2){
					
				}
				*/
				//Ignore this...
				continue;
			}
		}
		try{
			file.close();
		}catch(Exception e){
			
		}
	}
	
	public boolean search_BBlock_for_ReturnStmt(Unit searching){
		do{
			
			try{
				ReturnStmt is_searching_a_return_statment = (ReturnStmt) searching;
				return true; //If this does not throw an exception, it is.
			}catch(Exception e1){
				try{
					ReturnVoidStmt is_searching_a_void_return_statment = (ReturnVoidStmt) searching;
					return true; //If this does not throw an exception, it is.
				}catch(Exception e2){
					//Nope, but continue.
				}
			}
			
			if(g.getSuccsOf(searching).size() == 1){
				//We go down the only path available to g, since the previous if statement guaranteed that this does not have multiple possible successors
				searching = g.getSuccsOf(searching).get(0);
			}else{
				//Two possibilities:
				//BBlock ends with a branch. The BBlock ended without finding a return.
				//OR
				//No more successors, nothing to search.
				return false;
			}
		}while(searching != null);
		//Looking at a non-existent node? No point anymore.
		return false;
	}
}

