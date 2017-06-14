import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class PointerHeuristic extends BodyTransformer {
	private PatchingChain<Unit> units;
	private FileOutputStream file;
	HeuristicDatabase h_d;
	int h_id;
	
	PointerHeuristic(HeuristicDatabase hd, int heuristic_id){
		System.out.println("Pointer Heuristic Prepared.");
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
			file = new FileOutputStream("Soot_Heuristic_Information/pointer_h_" + b.getMethod(), false);
		}catch(Exception e){
			
		}		

		printAndWriteToFile("Applying " + phaseName + " on " + b.getMethod());
		
		//this is the patchingchain of unit in the body
		units = b.getUnits();
		
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				IfStmt ifStatement = (IfStmt) u1;
				
				//If we can turn either one of these operands in the condition of the if statement into a primitive type, the pointer heuristic will predict uncertain
				if(isPrimitive(((BinopExpr)ifStatement.getCondition()).getOp1().getType()) || isPrimitive(((BinopExpr)ifStatement.getCondition()).getOp2().getType())){
					//printAndWriteToFile("\t\tPrediction uncertain.");
				}else{
					//Right now we confirmed that both of them are not primitive.
					if(isObject(((BinopExpr)ifStatement.getCondition()).getOp1().getType()) && isObject(((BinopExpr)ifStatement.getCondition()).getOp2().getType())){
						//strangely enough, null is considered an RefLikeType.
						printAndWriteToFile("\tIfStmt Found: " + ifStatement);
						printAndWriteToFile("\t\tPredict untaken due to comparisons between two pointers or a pointer and null_type.");
					}
				}
			}catch(Exception e1){
				continue;
			}
		}
		try{
			file.close();
		}catch(Exception e){
			
		}
	}
	
	private boolean isPrimitive(Type what){
		try{
			PrimType is_what_a_primitive_type = (PrimType) what;
			return true;
		}catch(Exception e1){
			return false;
		}
	}
	private boolean isObject(Type what){
		try{
			RefLikeType is_what_a_RefLikeType_type = (RefLikeType) what;
			return true;
		}catch(Exception e1){
			return false;
		}
	}
}

