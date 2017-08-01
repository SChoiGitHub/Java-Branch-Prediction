import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;

import soot.options.CGOptions;
import soot.options.Options;
import soot.toolkits.astmetrics.ClassData;

import java.util.Scanner;

//For any future researchers
public class GeneralSootUse{
	//This has the main method, and thusly should be used as the caller.
	public static void main(String[] args) {
		HeuristicDatabase h_d = new HeuristicDatabase(8);
		
		try{			
			//The main must be called in order to add the internalTransforms below to the jtp pack. This will activate the Transform when Soot makes its Jimple file.
			//This Transform helps with reading the if statements.
			
			//PackManager.v().getPack("jtp").add(new Transform("jtp.if_reader",new IfReader()));
			//PackManager.v().getPack("jtp").add(new Transform("jtp.debug_bci",new DebugBCI()));
			
			//These transform should deal with analysis.
			
			add_jtp_Transformer(new BackHeuristic(h_d,0));
			add_jtp_Transformer(new ForwardHeuristic(h_d,1));
			add_jtp_Transformer(new ReturnHeuristic(h_d,2));
			add_jtp_Transformer(new CallHeuristic(h_d,3));
			add_jtp_Transformer(new PointerHeuristic(h_d,4));
			add_jtp_Transformer(new OpcodeHeuristic(h_d,5));
			add_jtp_Transformer(new LoopHeaderHeuristic(h_d,6));
			add_jtp_Transformer(new LoopEscaperHeuristic(h_d,7));
		}catch(Exception e){
			
		}
		
		
		soot.Main.main(args); //old way of doing things.
		h_d.print();
	}
	
	public static void add_jtp_Transformer(BodyTransformer a){
		PackManager.v().getPack("jtp").add(new Transform("jtp." + a.getClass().getName(),a));
	}
}
