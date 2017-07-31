This may be missing the "dacapo-9.12-bach.jar" file needed to run dacapo. You will need to download it yourself.

How to use "static_data_generator" on any java program without reflection.
1. use "bash compile.sh" in the "static_data_generator" folder.
	This assumes you have a java JDK installed.
2. use "bash run.sh", followed two arguements: the first one should be the program that runs soot while the second arguement should be the program that runs on soot. The arguments should be only the class names. No file extensions like ".java" or ".class" should be in the arguments.
	This uses two location varibles: $RT_JAR_LOCATION and $JCE_JAR_LOCATION.
	$RT_JAR_LOCATION should point at the rt.jar and the $JCE_JAR_LOCATION should point at the jce.jar
3. the static data should be generated. It will have the same name as the second argument, but it will have "_sd" at its end.
4. you may use "bash clean.sh" to clean up the files.

How to use "static_data_generator" on a DaCapo benchmark
1. use "bash update.sh" in the "Tamiflexer" folder.
	This assumes you have a java JDK installed.
2. bash the corresponding test of the dacapo benchmark you may want to check. It needs one argument to determine its size (small or default or large).
	This uses two location varibles: $RT_JAR_LOCATION and $JCE_JAR_LOCATION.
	$RT_JAR_LOCATION should point at the rt.jar and the $JCE_JAR_LOCATION should point at the jce.jar
3. the static data should be generated. It will have the same name as the second argument, but it will have "_sd" at its end.

IMPORTANT NOTES:
1. My bash scripts assume a certain naming structure. 
	('ClassName' + "_sd") = static data for the class, generated using static data generator.
	('ClassName' + "_pd") = profile data for the class, generated using profile data generator.
	('ClassName' + "_cd") = combined data for the class, generated using data aggregator.
2. If you want to use a different naming structure, you will have to edit my code.
3. Test command-line-prompts are given in "command-line-prompts.txt"

How to use the "profile_data_generator"
1. You need to ask for the program from your research mentor. (I am assuming you are taking up this project and have the same research mentor as me.)
2. When you get access and instructions on how to use it on any program, you need to put its output into a file. You can do this by putting " > filename_pd" at the end of the command that would normally run it.

How to use the "data_aggregator"
1. The data aggregator needs the static data and the profile data.
2. You need to make the cpp program.
	This assumes you have g++ installed.
3. bash "run_data_aggregator.sh" with an arguement that is the name of the program with data. For example, if I want to analyze a program named "avrora", there has to be a file named "avrora_sd" (sd = static data) and a file named "avrora_pd" (pd = profile data). The _sd and _pd files should contain static and profile data respectively.


How would I add a new heuristic to the static data generator?
1. Look at my heuristics first. You can base off your custom ones with my ones.
2. All heuristics act on bodies, which are soot's way of representing methods. Methods know what units (soot's version of statements) they contain. The information they know can be found in the JavaDoc at soot's GitHub.

How was the soot-trunk source code edited for this project?
Important changes have been made to "AsmMethodSource.java" so that it would keep track of the current opcode and assign them to an unit (which was modified to hold BCI information). The units would then be used to make jimple statements, which could be associated to profiling data later. "Blocks", "DirectedCallGraph", and "SootMethod" have been modified for frequency analysis.

Important Specific Changes to Classes
"Unit"
	public int get_BCI() //Gets the BCI of this unit. Sometimes appears as -1 if the unit cannot be associated with an opcode in the class.
	public void set_BCI(int) //Sets the BCI of this unit. This might not have uses outside of the AsmMethodSource class.
"Block"
	public ArrayList<SootMethod> whoDoICall() //Returns a list of methods that this block calls.
	public void calls(SootMethod sm) //Adds a method to the list that the method calls.
	public Boolean visited() //get boolean varible 'visited'
	public void visit() //set boolean varible 'visited' to true
	public void unvisit() //set boolean varible 'visited' to false
	public void setEdgeProb(Block b, double d) //set the edge probability from this block to 'b' to double 'd'
	public double getEdgeProb(Block to) //gets the edge probability from this block to 'b'
	public void setEdgeFreq(Block b, double d) //set the edge freq from this block to 'b' to double 'd'
	public double getEdgeFreq(Block to) //gets the edge freq from this block to 'b'
	public void intializeBackEdgeProb() //easy set-up of back edge prob
	public void setBackEdgeProb(Block b, double d) //set the back edge prob from this block to block 'b' to double 'd'
	public double getBackEdgeProb(Block to) //get the back edge prob from this block to block 'b'
	public void setBlockFreq(double d) //sets this blocks freq to double 'd'
	public double getBlockFreq() //gets this blocks freq
"DirectedCallGraph"
	public void setLocalFreq(SootMethod from, SootMethod to, Double what) //sets the local frequency from block 'from' to block 'to' to double 'what'
	public double getLocalFreq(SootMethod from, SootMethod to) //gets the local frequency from block 'from' to block 'to'
	public void setBackEdgeProb(SootMethod from, SootMethod to, Double what) //sets the back edge prob from block 'from' to block 'to' to double 'what'
	public double getBackEdgeProb(SootMethod from, SootMethod to) //gets the back edge prob from block 'from' to block 'to'
    
