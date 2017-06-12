import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

public class Tallier{
	private Hashtable<Unit,boolean[]> unitToTaken;
	int heuristic_count;
	
	Tallier(int h_c){
		unitToTaken = new Hashtable<Unit,boolean[]>();
		heuristic_count = h_c;
	}
	
	public void add(Unit u, int heuristic_num, boolean taken){
		if(!unitToTaken.containsKey(u)){
			unitToTaken.put(u,new boolean[heuristic_count]);
		}
		
		(unitToTaken.get(u))[heuristic_num] = taken;
	}
}

