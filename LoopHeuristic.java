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
	
	LoopHeuristic(){
		System.out.println("Loop Heuristic Prepared.");
	}
	
    protected void internalTransform (Body b, String phaseName, Map options){
		System.out.println("Applying " + phaseName + " on " + b.getMethod());
		
		super.internalTransform(b,phaseName,options);
		//If we want the loops, we can call super.loops() to have it return a collection of loops
		//this will print out all of the header units then print out all exits from that loop.
		for(Loop l : super.loops()){
			System.out.println("\tLoop Found, Header: " + l.getHead());
			System.out.println("\t\tExits:");
			for(Stmt s : l.getLoopExits()){
				System.out.println("\t\t" + s);
				try{
					//Is s an if statement?
					IfStmt s_if = (IfStmt) s;
					//If both are true, we check.
					if(s_if.getTarget() != l.getHead()){
						//The header is not the destination of the goto if it is taken. 
						System.out.println("\t\t\tPredict not taken to continue the loop.");
					}else{
						System.out.println("\t\t\tPredict taken to continue the loop.");
					}
				}catch(Exception e1){
					/*
					try{
						//Is this a switch statment?
						SwitchStmt s_sw = (SwitchStmt) s;
						System.out.println("Switch Heuristics are not accounted for yet.");
					}catch(Exception e2){
						//Okay, I don't know how to resolve this yet.
						System.out.println("\t\t\tThere was an unknown Heuristic Failure");				
					}	
					* */					
				}
				
			}
		}
    }
}
