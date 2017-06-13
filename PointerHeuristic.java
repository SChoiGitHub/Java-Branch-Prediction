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
	PatchingChain<Unit> units;
	
	
	PointerHeuristic(){
		System.out.println("Pointer Heuristic Prepared.");
	}
	
	protected void internalTransform(Body b, String phaseName, Map options){
		//this is the patchingchain of unit in the body
		units = b.getUnits();
		
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				IfStmt ifStatement = (IfStmt) u1;
				//Look at the successor of the IfStmt and check its position
				System.out.println("\tIfStmt Found: " + ifStatement);
				
				
				//If we can turn either one of these operands in the condition of the if statement into a primitive type, the pointer heuristic will predict uncertain
				if(isPrimitive(((BinopExpr)ifStatement.getCondition()).getOp1().getType()) || isPrimitive(((BinopExpr)ifStatement.getCondition()).getOp2().getType())){
					System.out.println("\t\tPrediction uncertain.");
				}else{
					//Right now we confirmed that both of them are not primitive.
					if(isObject(((BinopExpr)ifStatement.getCondition()).getOp1().getType()) && isObject(((BinopExpr)ifStatement.getCondition()).getOp2().getType())){
						//strangely enough, null is considered an RefLikeType.
						System.out.println("\t\tPrediction untaken due to comparisons between two pointers or a pointer and null_type.");
					}
				}
			}catch(Exception e1){
				continue;
			}
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

