package main;

import java.util.ArrayList;

public class Colony {

	private ArrayList<Ant> ants;
	private int numPassed = 0;
	private int numDead = 0;
	
	public Colony(){
		ants = new ArrayList<Ant>();
	}
	
	/**
	 * Run a specified number of ants through the graph
	 * @return the path of the last ant
	 */
	public ArrayList<Edge> swarm(Graph g, int numAnts){
		Ant a = new Ant(g,g.getEnd());
		for(int i = 0; i < numAnts; i++){
			a = new Ant(g, g.getEnd());
			ants.add(a);
			int status = 0;
			while(status == 0){
				status = a.move();
			}
			if(status == -1){
				numDead++;
			}
			else{
				numPassed++;
			}
		}
		return a.getPath();
	}

	/**
	 * Run an unlimited number of ants through the graph until the same path is found a specified number of times in a row
	 * @return the optimized path
	 */
	public ArrayList<Edge> swarmUntilStatic(Graph g, int threshold){
		ArrayList<Edge> path = new ArrayList<Edge>();
		int numConsecutiveSamePaths = 0;
		while(numConsecutiveSamePaths < threshold){
			Ant a = new Ant(g, g.getEnd());
			int status = a.walk();
			if(status == -1){
				numDead++;
				numConsecutiveSamePaths = 0;
				path = null;
			}
			else{
				if(path == null || !a.getPath().equals(path)){
					numConsecutiveSamePaths = 0;
				}
				else{
					numConsecutiveSamePaths++;
				}
				numPassed++;
				path = a.getPath();
			}
			g.evaporate();
		}
		return path;
	}
	
	public int getNumDead(){
		return numDead;
	}
	
	public int getNumPassed(){
		return numPassed;
	}
	
	public int getNumRuns(){
		return numDead + numPassed;
	}
}
