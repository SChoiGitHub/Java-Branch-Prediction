import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.JastAddJ.NumericType;
import soot.jimple.internal.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class OpcodeHeuristic extends HeuristicBase {
	private BriefBlockGraph g;
	
	OpcodeHeuristic(HeuristicDatabase hd, int heuristic_id){
		super(hd,heuristic_id);
	}
	
	protected void internalTransform(Body b, String phaseName, Map options){
		//System.out.println("Applying " + phaseName + " on " + b.getMethod());
		hd.name_heuristic(h_id,phaseName);
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefBlockGraph(b);
		
		for(Block block : g){
			if(g.getSuccsOf(block).size() == 2){
				if(block.getTail() instanceof IfStmt){
					//cond = condition
					if(((IfStmt)block.getTail()).getCondition() instanceof BinopExpr){
						BinopExpr cond = (BinopExpr) ((IfStmt)block.getTail()).getCondition();
						//System.out.println(cond + "\t" + cond.getClass().getName());
						//System.out.println("\t" + cond.getOp1() + "\t" + cond.getOp1().getClass().getName());
						//System.out.println("\t" + cond.getSymbol());
						//System.out.println("\t" + cond.getOp2() + "\t" + cond.getOp2().getClass().getName());
						switch(cond.getSymbol()){
							case " < ":
							case " <= ":
								if(cond.getOp2().toString().equals("0")){
									hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
								}
								break;
							case " > ":
							case " >= ":
								if(cond.getOp1().toString().equals("0")){
									hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
								}
								break;
							case " == ":
								if(
									(cond.getOp1() instanceof soot.jimple.Constant && cond.getOp2() instanceof soot.jimple.internal.JimpleLocal) ||
									(cond.getOp2() instanceof soot.jimple.Constant && cond.getOp1() instanceof soot.jimple.internal.JimpleLocal)
								){
									hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
								}
								break;
						}
					}
				}
			}
		}
	}

}

