import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.jimple.toolkits.annotation.*;
import soot.jimple.toolkits.annotation.logic.*;
import java.util.concurrent.*;

//For any future researchers
//This is a class that inheirits from LoopFinder, which inheirits from BodyTransform. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class LoopHeaderHeuristic extends HeuristicBase {
	private static Object lock = new Object();
	private static BriefUnitGraph bug;
	
	LoopHeaderHeuristic(HeuristicDatabase hd, int heuristic_id){
		super(hd,heuristic_id);
	}
	
    protected void internalTransform (Body b, String phaseName, Map options){
		synchronized(lock){
			//System.out.println("Applying " + phaseName + " on " + b.getMethod());
			
			bug = new BriefUnitGraph(b);
			
			
			hd.name_heuristic(h_id,phaseName);
			
			super.internalTransform(b,phaseName,options);
			//If we want the loops, we can call super.loops() to have it return a collection of loops
			//this will print out all of the header units then print out all exits from that loop.
			for(Loop l : super.loops()){
				for(Stmt s : l.getLoopExits()){
					//System.out.println(s.getClass().getName());
					
					try{
						//Is s an if statement?
						IfStmt s_if = (IfStmt) s;
						//If both are true, we check.
						if(s_if.getTarget().equals(l.getHead())){
							hd.add(b.getMethod(),h_id,true,s_if);
						}
					}catch(Exception e1){
						/*
						try{
							//Is this a switch statment?
							SwitchStmt s_sw = (SwitchStmt) s;
							printAndWriteToFile("Switch Heuristics are not accounted for yet.");
						}catch(Exception e2){
							//Okay, I don't know how to resolve this yet.
							printAndWriteToFile("\t\t\tThere was an unknown Heuristic Failure");				
						}	
						* */					
					}
					
				}
			}
		}
    }
}
