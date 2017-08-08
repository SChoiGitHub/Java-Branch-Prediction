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
//WARNING: I am not sure if this is usable in Java.
public class GuardHeuristic extends HeuristicBase {
	private BriefBlockGraph g;
	
	GuardHeuristic(HeuristicDatabase hd, int heuristic_id){
		super(hd,heuristic_id);
	}
	
	protected void internalTransform(Body b, String phaseName, Map options){
		//WARNING: I am not sure if this works!
		
		//System.out.println("Applying " + phaseName + " on " + b.getMethod());
		hd.name_heuristic(h_id,phaseName);
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		g = new BriefBlockGraph(b);
		
		for(Block block : g){
			if(g.getSuccsOf(block).size() == 2){
				if(block.getTail() instanceof IfStmt){
					//cond = condition
					//System.out.println(b.getMethod().getName() + "\t"  + block.getTail().get_BCI());
					
					if(((IfStmt)block.getTail()).getCondition() instanceof BinopExpr){
						BinopExpr cond = (BinopExpr) ((IfStmt)block.getTail()).getCondition();
						//Just debugging down here.
						//System.out.println(cond + "\t" + cond.getClass().getName());
						//System.out.println("\t" + cond.getOp1() + "\t" + cond.getOp1().getClass().getName());
						//System.out.println("\t" + cond.getSymbol());
						//System.out.println("\t" + cond.getOp2() + "\t" + cond.getOp2().getClass().getName());
						Block jump_block = null;
						
						for(Block succ : g.getSuccsOf(block)){
							if(succ.getHead().equals(((IfStmt)block.getTail()).getTarget())){
								jump_block = succ;
							}
						}
						
						if(jump_block != null){
							if(cond.getOp1() instanceof soot.jimple.internal.JimpleLocal){
								if(found_local_in_block(block,(soot.jimple.internal.JimpleLocal)cond.getOp1())){
									hd.add(b.getMethod(),h_id,true,(IfStmt)block.getTail());
									continue;
								}
							}
							if(cond.getOp2() instanceof soot.jimple.internal.JimpleLocal){
								if(found_local_in_block(block,(soot.jimple.internal.JimpleLocal)cond.getOp2())){
									hd.add(b.getMethod(),h_id,true,(IfStmt)block.getTail());
									continue;
								}
							}
						}
					}
				}
			}
		}
	}



	public boolean found_local_in_block(Block searching, soot.jimple.internal.JimpleLocal local){
		for(Iterator<Unit> i = searching.iterator(); i.hasNext();){
			Unit u = (Unit) i.next();
			for(ValueBox vb : u.getUseBoxes()){
				//We look at all the values that are used in u.
				if(contains_local(vb.getValue(),local)){
					//if we can find the local in that, we can say yes.
					return true;
				}
			}
		}
		return false;
	}

	public boolean contains_local(Value v, soot.jimple.internal.JimpleLocal local){
		if(v.equals(local)){
			//If local is value, we say yes.
			return true;
		}else if(v.getUseBoxes().size() != 0){
			//Otherwise, this value could contain other value.
			for(ValueBox vb : v.getUseBoxes()){
				//We go into each value that is in v.
				if(contains_local(vb.getValue(),local)){
					//This is recursive.
					return true;
				}
			}
		}
		return false;
	}

}

