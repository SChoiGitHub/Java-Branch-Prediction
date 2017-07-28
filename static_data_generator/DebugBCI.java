import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.jimple.internal.*;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class DebugBCI extends BodyTransformer {
	private PatchingChain<Unit> units;
	
	DebugBCI(){
		System.out.println("Debugging BCIs.");
	}

	protected void internalTransform(Body b, String phaseName, Map options){
		System.out.println(b.getMethod().getName());
		int if_num = -1;
		PatchingChain<Unit> units = b.getUnits();
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			System.out.println(u1.get_BCI() + "\t" + u1);
		}
	}
}

