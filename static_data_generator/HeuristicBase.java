import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.jimple.internal.*;

import soot.jimple.toolkits.annotation.*;
import soot.jimple.toolkits.annotation.logic.*;

import java.util.concurrent.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
//The heuristic base is the class that all heuristics should inheirit from for easier management
public abstract class HeuristicBase extends LoopFinder {
	protected HeuristicDatabase hd;
	protected int h_id;
	
	HeuristicBase(HeuristicDatabase hd_in, int h_id_in){
		hd = hd_in;
		h_id = h_id_in;
	}

}

