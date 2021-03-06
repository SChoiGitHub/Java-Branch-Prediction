import soot.*;
import soot.tagkit.*;
import soot.jimple.*;
import soot.util.*;
import java.io.*;
import java.util.*;
import soot.toolkits.graph.*;
import soot.jimple.internal.*;
import soot.options.Options;
import soot.jimple.toolkits.annotation.purity.*;
import soot.jimple.toolkits.callgraph.*;

import soot.jimple.toolkits.annotation.*;
import soot.jimple.toolkits.annotation.logic.*;

import java.util.concurrent.*;
import java.util.Scanner;
import java.util.ArrayList;

import soot.jimple.toolkits.callgraph.*;

//For any future researchers
//This is a class that inheirits from LoopFinder. This will allow it to be inserted into Soot's Packs, which will be dealt with at runtime.
//This handles the probability calculations of each method using combined data ("_cd") and static data ("_sd")
//When this is compiled, there will be warnings, I am not sure how to suppress them, but you can ignore them.
public class CalculateProbability extends LoopFinder {
	private BriefBlockGraph block_g;
	private PatchingChain<Unit> u;
	private MHGDominatorsFinder<Block> dominatorFinder_b;
	
	static private Hashtable<String,Double> heuristic_taken_probability;
	static private Hashtable<String,Hashtable<Integer,HeuristicInformation>> methodToPredictionTable;
	static private Hashtable<SootMethod,BriefBlockGraph> methodToBBG = new Hashtable<SootMethod,BriefBlockGraph>();
	static private int heuristic_count = -1;
	static private String[] what_heuristics_at_what_row;
	
	private static DirectedCallGraph dcg;
	private static MHGDominatorsFinder<SootMethod> dominatorFinder_sm;
	private static ArrayList<SootMethod> backEdgeTargets = new ArrayList<SootMethod>();
	
	static boolean built = false;
	
	public CalculateProbability(){
		
	}
	private boolean build(){
		//This should be called once to take in the information from the files.
		
		//Information on heuristic information
		if(Options.v().get_combined_data().equals("")){
			//There is no combined data file.
			throw new RuntimeException("You need a combined data file. Add \"-combined-data \'classname\'_cd\" to the line that calls ReadSpecificMethods.");
		}else{
			if(heuristic_taken_probability == null){
				//We have not made the item yet.
				try{
					Scanner f = new Scanner(new File(Options.v().get_combined_data()));
					//Open the file that is the combined data.
					heuristic_taken_probability = new Hashtable<String,Double>();
					//Make the hashtable
					while(f.hasNextLine()){
						String l = f.nextLine();
						Scanner string_stream = new Scanner(l);
						
						String what = string_stream.next();
						//Associate each heuristic with their taken rate.
						heuristic_taken_probability.put(what,Double.parseDouble(string_stream.next()));
					}
					System.out.println("Combined Data: " + Options.v().get_combined_data());
				}catch(Exception e){
					throw new RuntimeException("Failed to read combined data file. Add \"-static-data \'classname\'_sd\" to the line that calls ReadSpecificMethods.");
				}
			}
		}
		//rebuilding the heuristic prediction table from static file.
		if(!Options.v().get_static_data().equals("")){
			try{
				Scanner f = new Scanner(new File(Options.v().get_static_data()));
				methodToPredictionTable = new Hashtable<String,Hashtable<Integer,HeuristicInformation>>();
				boolean tablefound = false;
				boolean heuristic_name_found = false;
				String l = null;
				
				while(f.hasNextLine()){
					try{
						l = f.nextLine();
						if(heuristic_name_found && l.substring(0,8).equals("Method: ")){
							String method_name_for_this_set = l.substring(8,l.length());
							
							
							if(!methodToPredictionTable.containsKey(method_name_for_this_set)){
								methodToPredictionTable.put(method_name_for_this_set,new Hashtable<Integer,HeuristicInformation>());
							}
							
							l = f.nextLine();
							//System.out.println(method_name_for_this_set); //DEBUG
							//System.out.println(l); //DEBUG
							for(int x = Integer.parseInt(l.substring(21,l.length()))-1; x >= 0; x--){
								l = f.nextLine();
								
								Scanner s_s = new Scanner(l);
								int bci = Integer.parseInt(s_s.next());
								HeuristicInformation hi = new HeuristicInformation(heuristic_count);
								
								//System.out.print(bci + "\t"); //DEBUG
								
								for(int z = 0; z < heuristic_count; z++){
									hi.setTaken(z,Integer.parseInt(s_s.next()));
									//System.out.print(hi.getTaken(z) + "\t"); //DEBUG
								}
								//System.out.println(); //DEBUG
								methodToPredictionTable.get(method_name_for_this_set).put(bci,hi);
							}
						}else if(tablefound){
							heuristic_count = Integer.parseInt(l);
							what_heuristics_at_what_row = new String[heuristic_count];
							for(int x = 0; x < heuristic_count; x++){
								l = f.nextLine();
								what_heuristics_at_what_row[x] = l;
								//System.out.println(l);
							}
							heuristic_name_found = true;
						}else if(l.substring(0,6).equals("[ToIB]")){
							tablefound = true;
						}else if(l.substring(0,6).equals("[ToIE]")){
							break;
						}
					}catch(Exception e2){
						continue;
					}
				}
				System.out.println("Static Data: " + Options.v().get_static_data());
			}catch(Exception e){
				throw new RuntimeException("Failed to read static data.");
			}
		}else{
			throw new RuntimeException("You need a static data file.");
		}
		return true;
	}
	
	protected synchronized void internalTransform(Body b, String phaseName, Map options){
		//This occurs to each method within soot.
		if(!built){
			built = build();
		}
		
		//This is needed to get loops later.
		super.internalTransform(b,phaseName,options);
		
		//The method name has to be parsed to match it correctly.
		String method_name = parseMethod(b.getMethod());
		u = b.getUnits(); //we need to know the units.
		//System.out.println(fixThis); //DEBUG
		
		//We create a Control Flow Graph using b, the body of the SootMethod.
		block_g = new BriefBlockGraph(b);	
		
		//we need to know dominators for later.
		dominatorFinder_b = new MHGDominatorsFinder<Block> (block_g);
		
		//System.out.println("METHOD: " + b.getMethod()); //DEBUG
		
		//Just look at the paper, Algorithm 1. I copied it down and put it here.
		for(Block block : block_g){		
			if(methodToPredictionTable.containsKey(method_name) && methodToPredictionTable.get(method_name).containsKey(block.getTail().get_BCI())){
				//we have information for that block!
				
				//This block ends with a bci and has two successors.
				//Begin with half chances for taken and untaken.
				double taken = 0.5;
				double untaken = 0.5;
				
				try{
					//This is math, sorry. Just look at algorithm 1 in the paper.
					for(int x = 0; x < heuristic_count; x++){
						if(methodToPredictionTable.get(method_name).get(block.getTail().get_BCI()).getTaken(x) != 0){							
							double d = heuristic_taken_probability.get(what_heuristics_at_what_row[x]) * taken
									+ (1.0-heuristic_taken_probability.get(what_heuristics_at_what_row[x])) * (untaken);
							taken *= heuristic_taken_probability.get(what_heuristics_at_what_row[x])/d;
							untaken *= (1-heuristic_taken_probability.get(what_heuristics_at_what_row[x]))/d;
						}
					}
					//Assignment of the probabilities.
					for(Block succ : block.getSuccs()){
						//Just assign the taken and the untaken branches with what they get.
						if(succ.getHead().equals(((IfStmt)block.getTail()).getTarget())){
							//System.out.print("Taken "); //DEBUG: This is a taken block edge
							//The edge probability from "block" to "succ" is taken
							block.setEdgeProb(succ,taken);
						}else{
							//System.out.print("unTaken "); //DEBUG: This is an untaken block edge.
							//The edge probability from "block" to "succ" is taken
							block.setEdgeProb(succ,untaken);
						}
					}
				}catch(Exception e){
					//Whoops, I don't know what to do here, so we assume equal chance for both branches.
					System.out.println(e.getMessage());
					System.out.println("Defaulting both blocks to 0.5\t" + ((block.getTail())));
					for(Block succ : block.getSuccs()){
						block.setEdgeProb(succ,0.5);
					}
				}
				
				//System.out.println(); //for formatting DEBUG
				//System.out.println(block.getTail().get_BCI() + "\t" + taken + "\t" + untaken); //DEBUG
			}else{
				//We don't have information on this branch (which may acutally be non-binary). We assign equal chance to each branch.
				for(Block b1 : block.getSuccs()){
					block.setEdgeProb(b1,(1.0/block.getSuccs().size()));
				}
			}
		}
		
			
			
		//Just look at the paper, Algorithm 2. I copied it down and put it here.
		for(Block block : block_g){
			//Create back edges probabilities. We need it for later.
			block.intializeBackEdgeProb();
		}
		{
			//These are the loops. Put them in an array list.
			ArrayList<Loop> orderedLoops = new ArrayList<Loop>(loops());
			
			//lamda function to sort the loops. The one with the highest bci will be first.
			//Doing it from highest bci to lowest bci should get the inner loops to outer loops requirement of algorithm 2 done.
			orderedLoops.sort(
				(unit1,unit2) -> 
					(
						u.follows(unit1.getHead(),unit2.getHead()) ? -1 : 1
					)
			);
			
			for(Loop l : orderedLoops){
				//System.out.println("Found Loop " + l.getHead()); //DEBUG
				//This iterates between all loops from highest bci to lowest bci, which should deal with innermost loop to outermost loop
				//Yep, this subroutine is pretty much everything.
				propagateFrequencyPreparation(findBlockWithUnit(l.getHead()),l); //Unvisit all blocks in loop.
				propagateFrequency(findBlockWithUnit(l.getHead()),findBlockWithUnit(l.getHead())); //Propagate it!
			}
			for(Block h : block_g.getHeads()){
				propagateFrequencyPreparation(h); //Now unvisit everything.
				propagateFrequency(h,h); //Propagate frequency on everything.
			}
			
			//Save the graph. It is the one with the information and we need it later.
			methodToBBG.put(b.getMethod(),block_g);
		}
		
		
	}
	private void propagateFrequencyPreparation(Block b){
		//Mark every block in the CFG as unvisited.
		if(b.visited()){
			//System.out.println(l.getHead() + " " + b.getHead()); //DEBUG
			b.unvisit();
			for(Block s : block_g.getSuccsOf(b)){
				propagateFrequencyPreparation(s);
			}
		}
	}
	private void propagateFrequencyPreparation(Block b, Loop l){
		//Mark every block in the loop as unvisited.
		if(inLoop(b,l) && b.visited()){
			//If block b is in the loop, visit it.
			//System.out.println(l.getHead() + " " + b.getHead()); //DEBUG
			b.unvisit();
			for(Block s : block_g.getSuccsOf(b)){
				propagateFrequencyPreparation(s,l);
			}
		}
	}
	private void propagateFrequency(Block b, Block head){
		if(b.visited()){
			return; //Already done this block.
		}
		
		if(b.equals(head)){
			b.setBlockFreq(1.0); //The head is visited once.
		}else{
			//bp = predecessor
			for(Block bp : block_g.getPredsOf(b)){
				if(!bp.visited() && !isBackEdge(bp,b)){
					return;
				}
			}
			
			//Block frequency is zero for now.
			b.setBlockFreq(0.0);
			double cyclic_probability = 0.0;
			
			//bp = predecessor
			for(Block bp : block_g.getPredsOf(b)){
				if(isBackEdge(bp,b) && isLoopHead(b)){
					//Wait! This is a back edge! We will remember this.
					cyclic_probability += bp.getBackEdgeProb(b);
				}else{
					//add the this block freq for every incoming edge
					b.setBlockFreq(b.getBlockFreq() + bp.getEdgeFreq(b));
				}
			}
			
			//0.9999999999 is 1-epsilion, I think.
			if(cyclic_probability > (0.999999999999)){
				cyclic_probability = 0.999999999999;
			}
			
			b.setBlockFreq(
				//We just don't want to divide by zero okay?
				b.getBlockFreq() / (1-cyclic_probability)
			);
		}
		
		//Visit this block
		b.visit();
		
		//System.out.println("\t" + b.getBlockFreq() + "\t" + b.getHead()); //DEBUG
		
		//bi = successor
		for(Block bi : block_g.getSuccsOf(b)){
			//The edge frequency from 'b' to 'bi' is the edge proability multiplied by the 'b' frequency 
			b.setEdgeFreq(bi,
				b.getEdgeProb(bi) * b.getBlockFreq()
			);
			
			//System.out.println("\t\t" + b.getEdgeFreq(bi) + "\t" + bi.getHead()); //DEBUG
			
			if(bi.equals(head)){
				b.setBackEdgeProb(bi,
					b.getEdgeProb(bi) * b.getBlockFreq()
				);
			}
		}
		
		
		for(Block bi : block_g.getSuccsOf(b)){
			if(!isBackEdge(b,bi)){
				propagateFrequency(bi,head);
			}
		}		
	}
	private boolean isLoopHead(Block who){
		//The block here is a loop head?
		for(Loop l : loops()){
			if(l.getHead().equals(who.getHead())){
				return true;
			}
		}
		return false;
	}
	private boolean isBackEdge(Block from, Block to){
		//This is the definition of a back edge.
		return (dominatorFinder_b.isDominatedBy(from,to));
	}
	private Block findBlockWithUnit(Unit u){
		//Given a unit, find the block that contains the unit.
		for(Block b : block_g){
			for(Iterator<Unit> i = b.iterator(); i.hasNext(); ){
				if(i.next().equals(u)){
					return b;
				}
			}
		}
		return null;
	}
	private boolean inLoop(Block b, Loop l){
		//Is block b in loop l?
		for(Iterator<Unit> i = b.iterator(); i.hasNext(); ){
			Unit u0 = i.next();
			if(l.getLoopStatements().contains(u0)){
				//if I can find a statement in the block that is in the list of loop, I can say yes
				//System.out.println(u0 + "\t" + l.getLoopStatements()); //DEBUG
				return true;
			}
		}
		//Otherwise, its not in the loop
		return false;
	}
	private ArrayList<Block> getBackEdgeSuccs(Block b1){
		//Get a list of blocks that are backedge successors of b1
		ArrayList<Block> list = new ArrayList<Block>();
		for(Block b2 : b1.getSuccs()){
			//System.out.println(b2.getHead().get_BCI() + " followed by " + b1.getTail().get_BCI() + " " + u.follows(b2.getHead(), b1.getTail())); //DEBUG
			
			
			//b2 is after b1
			if(isBackEdge(b2,b1)){
				//The head of the successors must be before the tail of the branch off.
				list.add(b2);
			}
		}
		return list;
	}
	protected String parseMethod(SootMethod s_m){
		//This here helps analysis easier with the data aggregator.
		String fixThis = s_m.toString();
		for(int x = 0; x < fixThis.length(); x++){
			if(fixThis.charAt(x) == ':'){
				fixThis = fixThis.substring(1, x);
				fixThis += "." + s_m.getName();
			}
		}
		
		fixThis += "(";
		for(Type a : s_m.getParameterTypes()){
			//System.out.println(a + "\t" + a.getClass().getName());
			fixThis += typeAppend(a);
		}
		
		return fixThis + ")" + typeAppend(s_m.getReturnType());
	}
	protected String typeAppend(Type a){
		//The types in the profiling data are done this way.
		if(a instanceof IntType){
			return "I";
		}else if(a instanceof BooleanType){
			return "Z";
		}else if(a instanceof CharType){
			return "C";
		}else if(a instanceof LongType){
			return "J";
		}else if(a instanceof DoubleType){
			return "D";
		}else if(a instanceof ByteType){
			return "B";
		}else if(a instanceof FloatType){
			return "B";
		}else if(a instanceof ShortType){
			return "S"; //Unsure if this works
		}else if(a instanceof ArrayType){
			String returnme = "";
			for(int x = 0; x < ((ArrayType)a).numDimensions; x++){
				returnme += "[";
			}
			return returnme + typeAppend(((ArrayType)a).baseType);
		}else if(a instanceof VoidType){	
			return "V";
		}else if(a instanceof RefType){	
			return "L" + a.toString() + ';';
		}else{
			return "!@#ERROR#@!";
		}
	}
	public void callGraph(){
		//This is the callgraph algorithm
		//AKA Algorithm 3
		
		// the the call graph
		CallGraph cg = G.v().soot_Scene().getCallGraph();
		
		//We put in the call graph, a filter to accept all methods we have analyzed, the head of the entire program, and set the verbose status to false.
		dcg = new DirectedCallGraph(cg,new Filter(methodToBBG.keySet()),Scene.v().getEntryPoints().iterator(),false);
		
		//There is a warning here. Just ignore it
		dominatorFinder_sm = new MHGDominatorsFinder<SootMethod>(dcg);
		
		
		
		//Hey, I found loops!?
		for(Object o : dcg){
			if(o instanceof SootMethod){
				SootMethod sm = (SootMethod) o;
				if(methodToBBG.containsKey(sm)){
					for(Object p : dcg.getPredsOf(sm)){
						//does sm dominate its predicate? p > sm
						if(isBackEdge((SootMethod)p,sm)){
							for(Object o2 : dcg.getSuccsOf(p)){
								if(o2 instanceof SootMethod){
									//System.out.println(((SootMethod)o2).getName() + " is a target of a back edge"); //DEBUG
									//Algorithm 3 defines loop heads as targets of backedges
									backEdgeTargets.add((SootMethod)o2);
								}
							}
						}
					}
				}
			}
		}
		
		
		
		for(Object o : dcg){
			if(o instanceof SootMethod){
				//Soot method here.
				SootMethod sm = (SootMethod) o;
				if(methodToBBG.containsKey(sm)){
					//We have information about the method!
					for(Block b : methodToBBG.get(sm)){
						//Now, we go through EVERY unit in the block
						for(Iterator<Unit> i = b.iterator(); i.hasNext();){
							Unit u = i.next();
							//System.out.println(u.getClass().getName() + "\t" + u); //DEBUG
							if(u instanceof Stmt && ((Stmt)u).containsInvokeExpr()){
								//We create local frequencies using block frequencies.
								
								//"sm" is the "from"
								//"((Stmt)u).getInvokeExpr().getMethodRef().tryResolve()" is the "to"
								//"b.getBlockFreq()+dcg.getLocalFreq(sm,((Stmt)u).getInvokeExpr().getMethodRef().tryResolve()" is a long way of saying add the block frequency of the block that this statement is in to the local call frequency
								dcg.setLocalFreq(sm,
									((Stmt)u).getInvokeExpr().getMethodRef().tryResolve(),
									b.getBlockFreq()+dcg.getLocalFreq(sm,((Stmt)u).getInvokeExpr().getMethodRef().tryResolve())
								);
								b.calls(((Stmt)u).getInvokeExpr().getMethodRef().tryResolve());
								//System.out.println("\t"+sm + " CALLS " + ((Stmt)u).getInvokeExpr().getMethodRef().tryResolve() + "\tFREQ: " + b.getBlockFreq() + "\t" + dcg.getLocalFreq(sm,((Stmt)u).getInvokeExpr().getMethodRef().tryResolve())); //DEBUG
								
								//This is the first part of algorithm 3. Do the same thing that the previous thing did.
								dcg.setBackEdgeProb(sm,((Stmt)u).getInvokeExpr().getMethod(),b.getBlockFreq());
							}
						}
					}
				}
			}
		}
		
		for(Object o : dcg.getHeads()){
			if(o instanceof SootMethod){
				SootMethod sm = (SootMethod) o;
				propagateCallFreqPart1(sm); //This is "part one" of algorithm 3.
			}
		}
		
		for(Object o : dcg.getHeads()){
			if(o instanceof SootMethod){
				SootMethod sm = (SootMethod) o;
				entryUnvisitor(sm); //unvisit entry function and anything that it can reach.
				propagateCallFreq(sm,sm,true); //This should be the entry function.
			}
		}
		
	}	
	public void loopUnvisitor(SootMethod sm, SootMethod tracking){
		//does sm dominate the current node?
		
		if(dominatorFinder_sm.isDominatedBy(tracking, sm) && tracking.visited()){
			//It does! Its in the loop
			tracking.unvisit();
			
			for(Object succ : dcg.getSuccsOf(tracking)){
				//Look at the successors of this
				loopUnvisitor(sm,(SootMethod)succ);
			}
		}
		//System.out.println("LOOP END");
	}	
	public void entryUnvisitor(SootMethod sm){
		if(sm.visited()){
			//unvisit this if it is visited.
			sm.unvisit();
			//System.out.println("UNVISTED " + sm); //DEBUG
			for(Object succ : dcg.getSuccsOf(sm)){
				//then unvisit all successors
				entryUnvisitor((SootMethod) succ);
			}
		}
	}	
	public void propagateCallFreqPart1(SootMethod sm){
		sm.traverse(); //We were here.
		//For each function f...
		for(Object s : dcg.getSuccsOf(sm)){
			//We have not been here before right?
			if(s instanceof SootMethod && !((SootMethod)s).traversed()){
				//s_sm = successor sootmethod
				SootMethod s_sm = (SootMethod) s;				
				//We are getting the successors of sm. Add these to the stack.
				propagateCallFreqPart1(s_sm);
			}
		}		
		//... in reverse depth-first order.
		//This gets called once there are no more successors, we are doing a reverse depth-first order.
		if(isLoopHead(sm)){
			loopUnvisitor(sm,sm); //Mark the loop blocks as unvisited
			propagateCallFreq(sm,sm,false); //Then propagate the unvisited status amongst them.
		}
	}
	public void propagateCallFreq(SootMethod sm, SootMethod head, Boolean finale){
		if(sm.visited()){
			return; //We were here.
		}
		
		//System.out.println(parseMethod(sm) + "\t" + parseMethod(head) + "\t" + finale); //DEBUG
		
		
		
		/* //DEBUG INFORMATION
		System.out.println(sm.getName());
		System.out.println("\tPredicates");
		for(Object p : dcg.getPredsOf(sm)){
			
			if(p instanceof SootMethod){
				//p_sm = predicate sootmethod
				SootMethod p_sm = (SootMethod) p;
				System.out.println("\t\t" + p_sm.getName());
			}
		}
		System.out.println("\tSuccessors");
		for(Object s : dcg.getSuccsOf(sm)){
			if(s instanceof SootMethod){
				//s_sm = successor sootmethod
				SootMethod s_sm = (SootMethod) s;
				System.out.println("\t\t" + s_sm.getName());
			}
		}
		*/
		
		
		
		//let's find the invoke frequency!
		for(Object p : dcg.getPredsOf(sm)){
			if(p instanceof SootMethod){
				//p_sm = predicate sootmethod
				SootMethod p_sm = (SootMethod) p;
				if(!p_sm.visited() && !isBackEdge(p_sm,sm)){
					return;
				}
			}
		}
		
		if(sm.equals(head)){
			//We enter heads once.
			dcg.setInvokeFreq(sm,1.0);
		}else{
			//This is uncertain for now.
			dcg.setInvokeFreq(sm,0.0);
		}
		
		double cyclic_probability = 0.0;
		
		for(Object p : dcg.getPredsOf(sm)){
			if(p instanceof SootMethod){
				//p_sm = predicate sootmethod
				SootMethod p_sm = (SootMethod) p;
				
				
				//yes, final is finale here. Its because of keywords...
				if(finale && isBackEdge(p_sm,sm)){
					cyclic_probability += dcg.getBackEdgeProb(p_sm,sm);
					//System.out.println("BACKEDGE\t" + parseMethod(p_sm) + "\t" + parseMethod(sm) + "\t" + dcg.getBackEdgeProb(p_sm,sm) + "\t" + cyclic_probability);
				}else if(!isBackEdge(p_sm,sm)){
					dcg.setInvokeFreq(sm, dcg.getInvokeFreq(sm) + dcg.getGlobalCallFreq(p_sm,sm));
				}
				
			}
		}
		
		//0.9999999999 is 1-epsilion, I think.
		if(cyclic_probability > (0.999999999999)){
			//System.out.println("POPULAR\t" + parseMethod(sm));
			cyclic_probability = 0.999999999999;
			//System.out.println(parseMethod(sm) + " is really popular..."); //DEBUG
		}
		
		//Again, we don't want to divide by zero.
		dcg.setInvokeFreq(sm,   dcg.getInvokeFreq(sm) / (1.0-cyclic_probability)  );
		
		

		
		// CALCULATE GLOBAL CALL FREQ FOR sm's OUT EDGES
		
		sm.visit(); //We were here.
		
		for(Object s : dcg.getSuccsOf(sm)){
			if(s instanceof SootMethod){
				//s_sm = successor sootmethod
				SootMethod s_sm = (SootMethod) s;
				//The global call frequency is the local frequency and the call frequency multiplied
				dcg.setGlobalCallFreq(sm, s_sm, dcg.getLocalFreq(sm,s_sm) * dcg.getInvokeFreq(sm));
				
				//System.out.println("\t" + parseMethod(sm) + "\t" + parseMethod(s_sm) + "\t" + dcg.getGlobalCallFreq(sm,s_sm)); //DEBUG
				
				if(head.equals(s_sm) && !finale){
					//This will be important later.
					dcg.setBackEdgeProb(sm, s_sm, dcg.getLocalFreq(sm,s_sm) * dcg.getInvokeFreq(sm));
				}
			}
		}
		

		
		
		
		//System.out.println(sm.getName() + "\tInvokeFreq: " + dcg.getInvokeFreq(sm));
		
		//propagate to successors.
		for(Object s : dcg.getSuccsOf(sm)){
			if(s instanceof SootMethod){
				//s_sm = successor sootmethod
				SootMethod s_sm = (SootMethod) s;
				
				//System.out.println("\t"+ dcg.getLocalFreq(sm,s_sm) + "\tLocal Freq to: " + s_sm); //DEBUG
				//System.out.println("\t" +dcg.getGlobalCallFreq(sm,s_sm) + "\tGlobal Freq to: " + s_sm); //DEBUG
				
				if(!isBackEdge(sm,s_sm)){
					propagateCallFreq(s_sm,head,finale);
				}
			}
		}
	}
	public boolean isBackEdge(SootMethod from, SootMethod to){
		return (dominatorFinder_sm.isDominatedBy(from,to));
	}
	public boolean isLoopHead(SootMethod who){
		//Algorithm 3 defines Loop Heads as targets of back edges.
		//System.out.println(backEdgeTargets.lastIndexOf(who));
		return backEdgeTargets.contains(who);
		
		
		
		/*
		if(methodToBBG.containsKey(who)){
			for(Object p : dcg.getPredsOf(who)){
				SootMethod predicate = (SootMethod) p;
				if(dominatorFinder_sm.isDominatedBy(predicate, who)){
					//System.out.println(predicate.getName() + " is dominated by " + who.getName() + " and p to sm exists"); //DEBUGS
					return true;
				}
			}
		}
		return false;
		*/
	}
	public void writeInformation(){
		try{
			PrintWriter f = new PrintWriter(Options.v().get_combined_data().substring(0,Options.v().get_combined_data().length()-3) + "_frequencies.csv");
			
			//Generates a csv file. It uses tabs as its spacer.
			f.println("Edge Probability and Frequency");
			for(SootMethod sm : methodToBBG.keySet()){
				f.println("Method: " + parseMethod(sm));
				f.println("BCI of From Block's Tail\tFrom Block's Tail\tTo Block's Tail\tFrom Block's Frequency\tEdge Probability\tEdge Frequency\tWhat Methods Does the From Block Call?\tWhat Methods Does the To Block Call?");
				for(Block from : methodToBBG.get(sm)){
					for(Block to : methodToBBG.get(sm).getSuccsOf(from)){
						f.print(from.getTail().get_BCI() +"\t" + from.getTail() +"\t"+ to.getTail() +"\t"+ from.getBlockFreq() +"\t"+ from.getEdgeProb(to) +"\t"+ from.getEdgeFreq(to) + "\t");
						for(SootMethod called_soot_method : from.whoDoICall()){
							//whoDoICall is an addedMethod.
							f.print(parseMethod(called_soot_method) + " ");
						}
						f.print("\t");
						for(SootMethod called_soot_method : to.whoDoICall()){
							//whoDoICall is an addedMethod.
							if(methodToBBG.keySet().contains(called_soot_method)){
								f.print(parseMethod(called_soot_method) + " ");
							}
						}
						f.print("\n");
					}
				}
				f.println();
			}
			
			f.println("Call Frequencies");
			for(Object o : dcg){
				SootMethod sm = (SootMethod) o;
				
				f.println("From Method: " + parseMethod(sm) + "\tCall Frequency: " + dcg.getInvokeFreq(sm));
				f.println("To Method\tLocal Call To Frequency\tGlobal Call Frequency");
				
				for(Object o2 : dcg.getSuccsOf(sm)){
					SootMethod sm2 = (SootMethod) o2;
					f.println(parseMethod(sm2)+"\t"+dcg.getLocalFreq(sm,sm2)+"\t"+dcg.getGlobalCallFreq(sm,sm2));
				}
				
				f.println();
			}
			f.close();
		}catch(IOException e){
			
		}
	}
}
class Filter implements SootMethodFilter {
	static Set<SootMethod> methods;
	
	public Filter(Set<SootMethod> acceptables){
		methods = acceptables;
	}
	
	public boolean want(SootMethod method) {
		return methods.contains(method);
	}
}


