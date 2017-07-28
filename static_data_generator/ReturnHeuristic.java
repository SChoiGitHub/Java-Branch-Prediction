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
public class ReturnHeuristic extends HeuristicBase {
	private BriefBlockGraph g;
	
	ReturnHeuristic(HeuristicDatabase hd, int heuristic_id){
		super(hd,heuristic_id);
	}	
	
	protected void internalTransform(Body b, String phaseName, Map options){
		//System.out.println("Applying " + phaseName + " on " + b.getMethod());
		hd.name_heuristic(h_id,phaseName);
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefBlockGraph(b);
		
		//Iterate between all blocks
		for(Block block : g){
			if(g.getSuccsOf(block).size() == 2){
				if(block.getTail() instanceof IfStmt){
					for(Block succ : g.getSuccsOf(block)){
						if(((IfStmt)block.getTail()).getTarget().equals(succ.getHead()) && foundReturn(succ)){
							hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
						}
					}
				}
			}
		}
	}
	
	public boolean foundReturn(Block searching){
		for(Iterator<Unit> i = searching.iterator(); i.hasNext();){
			Unit u = (Unit) i.next();
			if(u instanceof ReturnStmt || u instanceof ReturnVoidStmt){
				//We found a returnstmt
				return true;
			}
		}
		return false;
	}
}

