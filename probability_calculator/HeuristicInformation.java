import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

//Proper documentation of this can be found in the static data generator version of this.
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

