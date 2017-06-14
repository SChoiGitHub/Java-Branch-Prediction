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
public class IfReader extends BodyTransformer {
	private PatchingChain<Unit> units;
	private FileOutputStream file;
	
	IfReader(){
		System.out.println("If Reader Prepared.");
	}

	protected void internalTransform(Body b, String phaseName, Map options){
		int if_num = 0;
		PatchingChain<Unit> units = b.getUnits();
		for(Unit u1 : units){
			//All Unit objects will be checked with their successors to see if they are actually after their successors in code.
			try{
				//If this is a jimple if statement, this will work. Else, it will throw an exception.
				JIfStmt ifStatement = (JIfStmt) u1;
				if_num++;
				System.out.println(if_num + " If: " + ifStatement);
			}catch(Exception e1){
			}
		}
	}
}

