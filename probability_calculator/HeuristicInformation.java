import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//Maybe we don't need this...
public class HeuristicInformation{
	private int[] heuristic_taken_or_not;
	private double taken_chance = -1;
	
	HeuristicInformation(int count){
		heuristic_taken_or_not = new int[count];
	}
	
	public void setTaken(int where, int what){
		heuristic_taken_or_not[where] = what;
	}
	
	public int getTaken(int where){
		return heuristic_taken_or_not[where];
	}
	
	public void setTakenChance(double in){
		taken_chance = in;
	}
}

