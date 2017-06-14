import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.jimple.toolkits.annotation.*;
import soot.jimple.toolkits.annotation.logic.*;

//For any future researchers
//This is a class that inheirits from LoopFinder, which inheirits from BodyTransform. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class LoopHeuristic extends LoopFinder {
	private FileOutputStream file;
	
	LoopHeuristic(){
		System.out.println("Loop Heuristic Prepared.");
	}
	
	public void printAndWriteToFile(String s){
		//System.out.println(s);
		try{
			file.write(s.getBytes());
			file.write('\n');
		}catch(Exception e){
			
		}
	}
	
    protected void internalTransform (Body b, String phaseName, Map options){
		try{
			file = new FileOutputStream("Soot_Heuristic_Information/loop_h_" + b.getMethod(), false);
		}catch(Exception e){
			
		}
		
		
		
		printAndWriteToFile("Applying " + phaseName + " on " + b.getMethod());
		
		super.internalTransform(b,phaseName,options);
		//If we want the loops, we can call super.loops() to have it return a collection of loops
		//this will print out all of the header units then print out all exits from that loop.
		for(Loop l : super.loops()){
			printAndWriteToFile("\tLoop Found, Header: " + l.getHead());
			
			for(Stmt s : l.getLoopExits()){
				printAndWriteToFile("\t\tExit: " + s);
				try{
					//Is s an if statement?
					IfStmt s_if = (IfStmt) s;
					//If both are true, we check.
					if(s_if.getTarget() != l.getHead()){
						//The header is not the destination of the goto if it is taken. 
						printAndWriteToFile("\t\t\tPredict not taken to continue the loop.");
					}else{
						printAndWriteToFile("\t\t\tPredict taken to continue the loop.");
					}
				}catch(Exception e1){
					/*
					try{
						//Is this a switch statment?
						SwitchStmt s_sw = (SwitchStmt) s;
						printAndWriteToFile("Switch Heuristics are not accounted for yet.");
					}catch(Exception e2){
						//Okay, I don't know how to resolve this yet.
						printAndWriteToFile("\t\t\tThere was an unknown Heuristic Failure");				
					}	
					* */					
				}
				
			}
		}
		try{
			file.close();
		}catch(Exception e){
			
		}
    }
}
