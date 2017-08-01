import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class CallHeuristic extends HeuristicBase {
	private BriefBlockGraph g;
	private MHGPostDominatorsFinder<Block> d;
		
		
	CallHeuristic(HeuristicDatabase hd, int heuristic_id){
		super(hd,heuristic_id);
	}

	protected synchronized void internalTransform(Body b, String phaseName, Map options){
		//System.out.println("Applying " + phaseName + " on " + b.getMethod());
		hd.name_heuristic(h_id,phaseName);
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefBlockGraph(b);
		//Finds Post dominators.
		d = new MHGPostDominatorsFinder<Block>(g);
		
		for(Block block : g){
			if(g.getSuccsOf(block).size() == 2){
				if(block.getTail() instanceof IfStmt){
					for(Block succ : g.getSuccsOf(block)){
						if(((IfStmt)block.getTail()).getTarget().equals(succ.getHead()) && validBlock(block,succ)){
							hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
						}
					}
				}
			}
		}
	}
	
	public boolean validBlock(Block b, Block succ){
		for(Iterator<Unit> i = succ.iterator(); i.hasNext();){
			Unit u = (Unit) i.next();
			if(u instanceof InvokeStmt){
				//Successor cannot post dominate the block b
				//Get post dominators of b, it CANNOT contain succ if it wants to apply.
				return !d.getDominators(b).contains(succ);
			}
		}
		return false;
	}
}

