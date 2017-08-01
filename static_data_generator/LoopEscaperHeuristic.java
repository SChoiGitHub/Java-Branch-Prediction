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
public class LoopEscaperHeuristic extends HeuristicBase {
	private static Object lock = new Object();
	
	LoopEscaperHeuristic(HeuristicDatabase hd, int heuristic_id){
		super(hd,heuristic_id);
	}
	
    protected void internalTransform (Body b, String phaseName, Map options){
		synchronized(lock){
			//System.out.println("Applying " + phaseName + " on " + b.getMethod());
			hd.name_heuristic(h_id,phaseName);
			super.internalTransform(b,phaseName,options);
			//If we want the loops, we can call super.loops() to have it return a collection of loops
			//this will print out all of the header units then print out all exits from that loop.
			for(Loop l : super.loops()){
				for(Stmt s : l.getLoopExits()){
					try{
						//Is s an if statement?
						IfStmt s_if = (IfStmt) s;
						//If both are true, we check.
						if(s_if.getTarget() != l.getHead()){
							//The header is not the destination of the goto if it is taken. 
							hd.add(b.getMethod(),h_id,false,s_if);
						}
					}catch(Exception e1){
						//Nope, s is not an ifstmt				
					}
					
				}
			}
		}
    }
}
