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

		
		CalculateProbability c_p = new CalculateProbability(); //We need access to this later as an object.
		PackManager.v().getPack("jtp").add(new Transform("jtp.calculate_probability",c_p)); //Add it to the pack.
		
		
		try{						
			List<ForcedMethodReader> load_these_in = new ArrayList<ForcedMethodReader>();
			try{
				//Basically read the files in.
				Scanner f = new Scanner(new File("../loadme.txt")); //The load me text file should contain the class you want and the method in the class you want to be analyzed.
				while(f.hasNextLine()){
					String l = f.nextLine();
					//String class_name = l.substring(0,l.indexOf('+')); //The class is to the left of the +
					//String method_name = l.substring(l.indexOf('+')+1,l.length()); //The method is to the right of the +
					//System.out.println(class_name + "\t\t" + method_name); //DEBUG
					load_these_in.add(new ForcedMethodReader(l.substring(0,l.indexOf('+')),l.substring(l.indexOf('+')+1,l.length()))); //Now read the method.
				}
			}catch(Exception e2){
				System.out.println("Whoops! We cannot read that class and/or that method");
			}
			
			Options.v().parse(args); //Parse Arguements; soot normally does this, but doing forced method reading means we need this.
			List<SootMethod> entryPoints = new ArrayList<SootMethod>(); //These are the entry points.
			
			for(int x = 0; x < load_these_in.size(); x++){
				//Basically load the classes and get the methods.
				SootClass c = Scene.v().forceResolve(load_these_in.get(x).class_name, SootClass.BODIES); 
				c.setApplicationClass();
				Scene.v().loadNecessaryClasses();
				entryPoints.add(c.getMethodByName(load_these_in.get(x).method_name)); //Add everything.
			}
			
			Scene.v().setEntryPoints(entryPoints); //These are the entry points
			PackManager.v().runPacks(); //Run the packs. This will run algorithm 1 and 2.
			c_p.callGraph(); //This is algorithm 3.
			c_p.writeInformation(); //Output information.
		}catch(Exception e1){
			System.out.println(e1.getMessage());
		}
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
