import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//Heuristic Information contains information on a specific ifstmt.
//It knows what heuristics apply to the ifstmt it is associated with.
/*
 * For example, I have three heuristics
 * BackHeuristic
 * ForwardHeuristic
 * OpcodeHeuristic
 * 
 * heuristic_taken_or_not will have a size of 3
 * 
 * heuristic_taken_or_not[0] corresponds to BackHeuristic
 * heuristic_taken_or_not[1] corresponds to ForwardHeuristic
 * heuristic_taken_or_not[2] corresponds to OpcodeHeuristic
 * 
 * The int there will represent taken (2), untaken (1), or not applied (0)
 * This could be modified for multi-way branches later on if needed, but that would require modifications to heuristic database.
 */
public class HeuristicInformation{
	private int[] heuristic_taken_or_not;
	
	HeuristicInformation(int count){
		heuristic_taken_or_not = new int[count];
	}
	
	public void setTaken(int where, int what){
		heuristic_taken_or_not[where] = what;
	}
	
	public int getTaken(int where){
		return heuristic_taken_or_not[where];
	}
}

