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
//When Soot is run, it runs all the transformations in its packs on each method in the class it acts upon.
//The heuristics are transformations, but they don't change anything, just analyze it.
//This is intended to be the first arguement in the run bash script.
//The main difference here is that this program reads in the "loadme.txt" file in the folder above this one.
//It does this so that it can read specific methods.
//For example, putting "LoopTest+main" will signal to this that it needs to go to LoopTest (the class name is to the left of the +) and forcefully read main (the method name in the class is to the right of the +)
//"avrora+main" would load in method "main" in class "avrora" for example.
//It would be easiest to make a bash script to read specific methods of a particular class.
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
		//addTransformer(new GuardHeuristic(h_d,6));
		
		try{			
			List<ForcedMethodReader> load_these_in = new ArrayList<ForcedMethodReader>(); //ForcedMethodReader is a struct.
			try{
				//Basically read the files in.
				Scanner f = new Scanner(new File("../loadme.txt")); //Look at loadme.txt in the folder above
				while(f.hasNextLine()){
					String l = f.nextLine();
					//String class_name = l.substring(0,l.indexOf('+')); //Get the class name.
					//String method_name = l.substring(l.indexOf('+')+1,l.length()); //Get the method name
					//System.out.println(class_name + "\t\t" + method_name); //DEBUG
					load_these_in.add(new ForcedMethodReader(l.substring(0,l.indexOf('+')),l.substring(l.indexOf('+')+1,l.length())));
				}
				
			}catch(Exception e2){
				//There was an error somewhere...
				System.out.println("Whoops! We cannot read that class and/or that method");
			}
			
			Options.v().parse(args); //Parse Arguements for Soot.
			List<SootMethod> entryPoints = new ArrayList<SootMethod>(); //These are the entry points that soot will have to acknowledge
			
			for(int x = 0; x < load_these_in.size(); x++){
				//Basically load the classes and get the methods.
				SootClass c = Scene.v().forceResolve(load_these_in.get(x).class_name, SootClass.BODIES);  //Get the class.
				c.setApplicationClass(); //This is needed I think. I am not sure why though.
				Scene.v().loadNecessaryClasses();
				entryPoints.add(c.getMethodByName(load_these_in.get(x).method_name)); //Add the method in the class.
			}
			
			Scene.v().setEntryPoints(entryPoints); //Soot will have to look at these.
			PackManager.v().runPacks(); //Run the packs and transformations
			
		}catch(Exception e1){
			//There was an error somewhere.
			System.out.println(e1.getMessage());
		}
		h_d.print(); //The heuristic database needs to print out its information after everything it run.
	}
	
	public static void addTransformer(BodyTransformer a){
		//This method loads the heuristic and gives it a standard name based on its class.
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
