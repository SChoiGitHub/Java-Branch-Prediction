import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
//This heuristic predicts that a comparison between an object reference (or pointer) OR null with something else will not be taken.
public class PointerHeuristic extends HeuristicBase {
	private BriefBlockGraph g;
	
	PointerHeuristic(HeuristicDatabase hd, int heuristic_id){
		super(hd,heuristic_id); //I need to call this.
	}
	
	protected void internalTransform(Body b, String phaseName, Map options){
		//System.out.println("Applying " + phaseName + " on " + b.getMethod()); //This is debug information.
		hd.name_heuristic(h_id,phaseName); //I give the name of this heuristic to the database.
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefBlockGraph(b);
		
		for(Block block : g){ //This iterates over ever block.
			if(g.getSuccsOf(block).size() == 2){ //check if it has only two successors
				if(block.getTail() instanceof IfStmt){ //we know that it is an ifstmt
					if(((IfStmt)block.getTail()).getCondition() instanceof BinopExpr){ //And know that it has a binary operator expression.
						//cond = condition
						BinopExpr cond = (BinopExpr) ((IfStmt)block.getTail()).getCondition();
						//The System.out.println are debug information
						//System.out.println(b.getMethod().getName());
						//System.out.println(cond + "\t" + cond.getClass().getName());
						//System.out.println("\t" + cond.getOp1() + "\t" + cond.getOp1().getType().getClass().getName());
						//System.out.println("\t" + cond.getSymbol());
						//System.out.println("\t" + cond.getOp2() + "\t" + cond.getOp2().getType().getClass().getName());
						if( (cond.getSymbol().equals(" == ")) &&
							(
								(cond.getOp1().getType() instanceof soot.RefLikeType && cond.getOp2().getType() instanceof soot.RefLikeType) ||
								(cond.getOp2().getType() instanceof soot.NullType || cond.getOp1().getType() instanceof soot.NullType)
							)
						){
							//We now know that the operator is comparing for equality between (two RefLikeType) OR (one NullType and something else)
							//We predict untaken.
							hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
						}
						break;
					}
				}
			}
		}
	}
}

