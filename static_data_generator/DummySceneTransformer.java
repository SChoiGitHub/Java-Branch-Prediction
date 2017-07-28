import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.JastAddJ.NumericType;

//For any future researchers
//This is a class that inheirits from BodyTransformer. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
public class DummySceneTransformer extends SceneTransformer {
	DummySceneTransformer(){
		System.out.println("SceneTransformer");
	}
	
	protected void internalTransform(String phaseName, Map options){
		
	}

}

