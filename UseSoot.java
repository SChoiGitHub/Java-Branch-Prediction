import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;




//For any future researchers
public class UseSoot{
	//This has the main method, and thusly should be used as the caller.
	public static void main(String[] args) {
		HeuristicDatabase h_d = new HeuristicDatabase(6);
		
		try{
			File directory = new File("Soot_Heuristic_Information");
			try{
				directory.mkdir();
			} 
			catch(Exception e){
			}   
			
			
			//The main must be called in order to add the internalTransforms below to the jtp pack. This will activate the Transform when Soot makes its Jimple file.
			//This Transform helps with reading the if statements.
			
			//PackManager.v().getPack("jtp").add(new Transform("jtp.if_reader",new IfReader()));
			
			//These transform should deal with analysis.
			PackManager.v().getPack("jtp").add(new Transform("jtp.back_h",new BackHeuristic(h_d,0)));
			PackManager.v().getPack("jtp").add(new Transform("jtp.loop_h",new LoopHeuristic(h_d,1)));
			PackManager.v().getPack("jtp").add(new Transform("jtp.return_h",new ReturnHeuristic(h_d,2)));
			PackManager.v().getPack("jtp").add(new Transform("jtp.call_h",new CallHeuristic(h_d,3)));
			PackManager.v().getPack("jtp").add(new Transform("jtp.pointer_h",new PointerHeuristic(h_d,4)));
			PackManager.v().getPack("jtp").add(new Transform("jtp.opcode_h",new OpcodeHeuristic(h_d,5)));
		}catch(Exception e){
			
		}
		
		
		//We have to call soot using the arguement. This would be no different from calling soot itself aside from the fact that we implemented transforms.
		soot.Main.main(args);
		
		h_d.print();
	}
}

