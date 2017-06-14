import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.JastAddJ.NumericType;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class OpcodeHeuristic extends BodyTransformer {
	private PatchingChain<Unit> units;
	private IfStmt ifStatement;
	private FileOutputStream file;
	HeuristicDatabase h_d;
	int h_id;
	
	OpcodeHeuristic(HeuristicDatabase hd, int heuristic_id){
		System.out.println("Opcode Heuristic Prepared.");
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
			file = new FileOutputStream("Soot_Heuristic_Information/opcode_h_" + b.getMethod(), false);
		}catch(Exception e){
			
		}	
		
		printAndWriteToFile("Applying " + phaseName + " on " + b.getMethod());
		
		//this is the patchingchain of unit in the body
		units = b.getUnits();
		
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				ifStatement = (IfStmt) u1;
				//Look at the successor of the IfStmt and check its position
				

				
				parse(((BinopExpr)ifStatement.getCondition()).getOp1(),((BinopExpr)ifStatement.getCondition()).getSymbol(),((BinopExpr)ifStatement.getCondition()).getOp2());
				/*
				//If we can turn either one of these operands in the condition of the if statement into a primitive type, the pointer heuristic will predict uncertain
				if(isConstant((Local)((BinopExpr)ifStatement.getCondition()).getOp1()) || isConstant((Local)((BinopExpr)ifStatement.getCondition()).getOp2())){
					//One of the operands is a nameless varible, which is likely a constant.
					printAndWriteToFile("\t\tPredict untaken because there is probably a comparison between something an a constant.");
				}else{
					
				}
				*/
				
			}catch(Exception e1){
				continue;
			}
		}
		try{
			file.close();
		}catch(Exception e){
			
		}
	}
	
	public void parse(Value l, String op, Value r){
		
		//printAndWriteToFile(l);
		//printAndWriteToFile(op);
		//printAndWriteToFile(r);
		
		
		boolean l_is_0 = false;
		boolean l_is_const = false;
		boolean l_is_int = false;
		boolean r_is_0 = false;
		boolean r_is_const = false;
		boolean r_is_int = false;
		try{
			if(((IntConstant)l).value == 0){
				//Hey! The left operand is zero!
				l_is_0 = true;
				r_is_const = true;
				r_is_int = true;
			}else{
				//Its still a constant. We parsed it and it worked.
				l_is_const = true;
				r_is_int = true;
			}
		}catch(Exception e1){
			try{
				Type are_you_an_int = (IntType)((Local) l).getType();
				l_is_int = true;
			}catch(Exception e2){
				//Not even a int.
			}
		}
		
		try{
			if(((IntConstant)r).value == 0){
				//Hey! The left operand is zero!
				r_is_0 = true;
				r_is_const = true;
				r_is_int = true;
			}else{
				//Its still a constant.
				r_is_const = true;
				r_is_int = true;
			}
		}catch(Exception e){
			try{
				Type are_you_an_int = (IntType)((Local) r).getType();
				r_is_int = true;
			}catch(Exception e2){
				//Not even a int.
			}
		}
		
		if(l_is_0){
			//Left is a constant zero.
			if(op.equals(" > ") || op.equals(" >= ")){
				//Now we know that left is being checked to see if it is greater (or maybe greater or equal to) than its other operand.
				if(r_is_int){
					printAndWriteToFile("\tIfStmt Found: " + ifStatement);
					printAndWriteToFile("\t\tPredict untaken due to the \"less than\" comparison of an integer and zero.");
				}
			}else{
				//Uh, we don't know now.
				//printAndWriteToFile("\t\tPrediction uncertain.");
			}
		}else if(r_is_0){
			//Right is a constant zero.
			if(op.equals(" < ") || op.equals(" <= ")){
				//Now we know that right is being checked to see if it is greater (or maybe greater or equal to) than its other operand.
				if(l_is_int){
					printAndWriteToFile("\tIfStmt Found: " + ifStatement);
					printAndWriteToFile("\t\tPredict untaken due to the \"less than\" comparison of an integer and zero.");
				}
			}else{
				//Uh, we don't know now.
				//printAndWriteToFile("\t\tPrediction uncertain.");
			}
		}else if(l_is_const != r_is_const){
			//Only one of these is a constant int.
			if((r_is_int && l_is_int) && op.equals(" == ")){
				//If both are an int and we know equality is being checked, predict untaken.
				printAndWriteToFile("\tIfStmt Found: " + ifStatement);
				printAndWriteToFile("\t\tPredict untaken due to the equality comparision of an int varible and zero.");
			}else{
				//printAndWriteToFile("\t\tPrediction uncertain.");
			}
		}else{
			//printAndWriteToFile("\t\tPrediction uncertain.");
		}
	}

}

