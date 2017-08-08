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
//This heuristic predicts that, if the ifstmt makes a comparison that is "something is less than zero" OR "something is equal to a constant", the ifstmt will not be taken.
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
			//Go to each block in the method
			if(g.getSuccsOf(block).size() == 2){
				//We have checked that the successor counter is two.
				if(block.getTail() instanceof IfStmt){
					//We have checked that the tail of the block is an ifstmt.
					//cond = condition
					//System.out.println(b.getMethod().getName() + "\t"  + block.getTail().get_BCI());
					if(((IfStmt)block.getTail()).getCondition() instanceof BinopExpr){
						//We have checked that the tail is a binary operator expression
						BinopExpr cond = (BinopExpr) ((IfStmt)block.getTail()).getCondition();
						//Just debugging down here.
						//System.out.println(cond + "\t" + cond.getClass().getName());
						//System.out.println("\t" + cond.getOp1() + "\t" + cond.getOp1().getClass().getName());
						//System.out.println("\t" + cond.getSymbol());
						//System.out.println("\t" + cond.getOp2() + "\t" + cond.getOp2().getClass().getName());
						switch(cond.getSymbol()){
							case " < ":
							case " <= ":
								if(cond.getOp2() instanceof soot.jimple.NumericConstant && (Double.parseDouble(((soot.jimple.NumericConstant)cond.getOp2()).toString())) == 0.0){
									//The second oprand is zero, which means the left opcode is being compared with zero. Predict untaken.
									hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
								}
								break;
							case " > ":
							case " >= ":
								if(cond.getOp1() instanceof soot.jimple.NumericConstant && (Double.parseDouble(((soot.jimple.NumericConstant)cond.getOp1()).toString())) == 0.0){
									//The first oprand is zero. The right opcode is being compared to zero. predict untaken.
									hd.add(b.getMethod(),h_id,false,(IfStmt)block.getTail());
								}
								break;
							case " == ":
								if(
									(cond.getOp1() instanceof soot.jimple.Constant && cond.getOp2() instanceof soot.jimple.internal.JimpleLocal) ||
									(cond.getOp2() instanceof soot.jimple.Constant && cond.getOp1() instanceof soot.jimple.internal.JimpleLocal)
								){
									//One of the operands is a varible (JimpleLocal) and the other is a constant (Constant). predict untaken.
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

