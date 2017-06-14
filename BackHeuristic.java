import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class BackHeuristic extends BodyTransformer {
	private FileOutputStream file;
	HeuristicDatabase h_d;
	int h_id;
	int if_num = -1;
	
	BackHeuristic(HeuristicDatabase hd, int heuristic_id){
		System.out.println("Back Heuristic Prepared.");
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
		
		
		try{
			file = new FileOutputStream("Soot_Heuristic_Information/back_h_" + b.getMethod(), false);
		}catch(Exception e){
			
		}	
		
		
		
		printAndWriteToFile("Applying " + phaseName + " on " + b.getMethod());
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		BriefUnitGraph g = new BriefUnitGraph(b);
		//units represents all the statements within the body. Local varibles and exceptions are in other chains.
		PatchingChain<Unit> units = b.getUnits();
		//Iterate between all Unit objects in units.
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is an if statement, this will work. Else, it will throw an exception.
				IfStmt ifStatement = (IfStmt) u1;
				if_num++;
				if(units.follows(u1,(Unit)ifStatement.getTarget())){ //Returns true if u1 is after u2
					printAndWriteToFile("\tIfStmt Found: " + ifStatement);
					printAndWriteToFile("\t\tThis is a back branch. Predict taken.");
					h_d.add(b.getMethod(),if_num,h_id,true);
				}else{
					printAndWriteToFile("\tIfStmt Found: " + ifStatement);
					printAndWriteToFile("\t\tThis is a forward branch. Predict not taken.");
					h_d.add(b.getMethod(),if_num,h_id,false);
				}
			}catch(Exception e){
				//Ignore this...
				continue;
			}
		}
		try{
			file.close();
		}catch(Exception e){
			
		}
	}
}
