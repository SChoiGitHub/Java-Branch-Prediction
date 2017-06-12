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
		super.internalTransform(b,phaseName,options);
		//If we want the loops, we can call super.loops() to have it return a collection of loops
		//this will print out all of the header units.
		for(Loop l : super.loops()){
			System.out.println("\tLoop Found, Header: " + l.getHead());
			System.out.println("\t\tExits:");
			for(Stmt s : l.getLoopExits()){
				System.out.println("\t\t" + s);
				System.out.println("\t\t\tPredict not taken to continue the loop.");
			}
		}
    }
}
