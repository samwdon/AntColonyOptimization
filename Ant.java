package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Ant {
	
	public static int DISTANCE_MAX;	//set in constructor so it is relative to graph size
	private static final double PHEROMONE_MAX = 10.0;
	private Node currentPos;
	private final int id;
	private static int numAnts = 0;
	private HashSet<Edge> traveledTo;
	private ArrayList<Edge> path;
	Graph graph;
	Node end;
	
	public Ant(Graph graph, Node end){
		DISTANCE_MAX = graph.getSize();
		this.graph = graph;
		this.end = end;
		id = numAnts;
		numAnts++;
		currentPos = graph.getStart();
		traveledTo = new HashSet<Edge>();
		path = new ArrayList<Edge>();
	}
	
	/**
	 * Move the ant one step until it either reaches the end or "dies"
	 * @return
	 */
	public int walk(){
		int status = move();
		while(status == 0){
			status = move();
		}
		return status;
	}
	
	/**
	 * Move the ant by one node
	 * @return An int representing the ant's status
	 * 		1: reached the end of the graph
	 * 		0: did not reach the end
	 * 		-1: traveled the max distance and "died"
	 */
	public int move(){
		ArrayList<Edge> adjacentEdges = graph.getEdgesConnectedTo(currentPos);	//all adjacent edges
		ArrayList<Edge> possibleMoves = new ArrayList<Edge>();		//edges the ant has not traversed yet
		for( Edge e : adjacentEdges){
			
			//if the finish is adjacent go there
			if(e.to() == end){
				currentPos = end;
				traveledTo.add(e);
				path.add(e);
				dropPheremone(path);
				return 1;
			}
			if(!traveledTo.contains(e)){
				possibleMoves.add(e);
			}
		}
		
		Edge next;
		//if there are no yet to be traveled edges choose one of the edges that has already been traversed
		if(possibleMoves.size() == 0){
			 next = choosePath(adjacentEdges);
		}
		//otherwise choose one of the yet to be traveled edges
		else{
			next = choosePath(possibleMoves);
		}
		
		currentPos = next.oppositeEnd(currentPos);
		traveledTo.add(next);
		path.add(next);
		if(path.size() == DISTANCE_MAX){
			return -1;
		}
		return 0;
	}
	
	/**
	 * Pick an edge to travel to next randomly using weighted probabilities based on pheromone strength
	 * @param edges
	 * @return
	 */
	public Edge choosePath(ArrayList<Edge> edges){
		double totalPheremone = 0;
		for(Edge e : edges){
			totalPheremone += e.getPheremoneLevel();
		}
		Random random = new Random();
		double max = totalPheremone * random.nextDouble();
		double sum = 0;
		int i = 0;
		while(sum < max){
			sum += edges.get(i).getPheremoneLevel();
			i++;
		}
		return edges.get(i-1);
	}
	
	/**
	 * Leave pheromone along a path
	 * Used after a successful path is found
	 * @param path
	 */
	public void dropPheremone(ArrayList<Edge> path){
		for(Edge e : path){
			e.raisePheremoneLevel(PHEROMONE_MAX / path.size());	//a shorter path leaves more pheromone per edge
		}
	}
	
	public ArrayList<Edge> getPath(){
		return path;
	}

}
