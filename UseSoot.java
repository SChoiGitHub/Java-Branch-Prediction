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
		Tallier t = new Tallier(3);
		
		
		//The main must be called in order to add the internalTransforms below to the jtp pack. This will activate the Transform when Soot makes its Jimple file.
		//These transform should deal with analysis.
		PackManager.v().getPack("jtp").add(new Transform("jtp.back_h",new BackHeuristic(t,0)));
		PackManager.v().getPack("jtp").add(new Transform("jtp.loop_h",new LoopHeuristic(t,1)));
		PackManager.v().getPack("jtp").add(new Transform("jtp.return_h",new ReturnHeuristic(t,2)));
		//We have to call soot using the arguement. This would be no different from calling soot itself aside from the fact that we implemented transforms.
		soot.Main.main(args);
	}
}

