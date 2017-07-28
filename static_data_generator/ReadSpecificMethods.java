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
public class ReadSpecificMethods{
	//This has the main method, and thusly should be used as the caller.
	public static void main(String[] args) {
		
		HeuristicDatabase h_d = new HeuristicDatabase(6);
		
		//addTransformer(new BackHeuristic(h_d,0));
		//addTransformer(new ForwardHeuristic(h_d,1));
		addTransformer(new ReturnHeuristic(h_d,0));
		addTransformer(new CallHeuristic(h_d,1));
		addTransformer(new PointerHeuristic(h_d,2));
		addTransformer(new OpcodeHeuristic(h_d,3));
		addTransformer(new LoopHeaderHeuristic(h_d,4));
		addTransformer(new LoopEscaperHeuristic(h_d,5));

		
		try{			
			List<ForcedMethodReader> load_these_in = new ArrayList<ForcedMethodReader>();
			try{
				//Basically read the files in.
				Scanner f = new Scanner(new File("../loadme.txt"));
				while(f.hasNextLine()){
					String l = f.nextLine();
					String class_name = l.substring(0,l.indexOf('+'));
					String method_name = l.substring(l.indexOf('+')+1,l.length());
					//System.out.println(class_name + "\t\t" + method_name); //DEBUG
					load_these_in.add(new ForcedMethodReader(l.substring(0,l.indexOf('+')),l.substring(l.indexOf('+')+1,l.length())));
				}
				
			}catch(Exception e2){
				System.out.println("Whoops! We cannot read that class and/or that method");
			}
			
			Options.v().parse(args); //Parse Arguements
			List<SootMethod> entryPoints = new ArrayList<SootMethod>();
			
			for(int x = 0; x < load_these_in.size(); x++){
				//Basically load the classes and get the methods.
				SootClass c = Scene.v().forceResolve(load_these_in.get(x).class_name, SootClass.BODIES); 
				c.setApplicationClass();
				Scene.v().loadNecessaryClasses();
				entryPoints.add(c.getMethodByName(load_these_in.get(x).method_name));
			}
			
			Scene.v().setEntryPoints(entryPoints);
			PackManager.v().runPacks();
			
		}catch(Exception e1){
			System.out.println(e1.getMessage());
		}
		h_d.print();
	}
	
	public static void addTransformer(BodyTransformer a){
		PackManager.v().getPack("jtp").add(new Transform("jtp." + a.getClass().getName(),a));
	}
}




class ForcedMethodReader{
	public String class_name;
	public String method_name;
	
	public ForcedMethodReader(String cn, String mn){
		this.class_name = cn;
		this.method_name = mn;
	}
}
