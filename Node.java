package main;

import java.util.ArrayList;
import java.util.HashSet;

public class Node implements Comparable<Node>{

	private static int numberOfNodes = 0;
	private int id;
	private HashSet<Node> adjacentNodes;
	public int distance; //used for dijkstras


	public Node(){
		id = numberOfNodes;
		numberOfNodes++;
		adjacentNodes = new HashSet<Node>();
	}

	/**
	 * Add the supplied Node to this Node's adjacent nodes and vice versa
	 */
	public void connect(Node other){
		adjacentNodes.add(other);
		other.getAdjacentNodes().add(this);
	}

	/**
	 * @return All Node's adjacent to this node in the form of an ArrayList
	 */
	public ArrayList<Node> getAdjacentNodeList(){
		ArrayList<Node> list = new ArrayList<Node>();
		for( Node n: adjacentNodes){
			list.add(n);
		}
		return list;
	}
	
	/**
	 * Compare Node's distance to other Node's distance, used in Dijkstras
	 * @return 1 if distance is greater, -1 if is is smaller, 0 if they have the same distance
	 */
	public int compareDistances(Node other) {
		if(other == null){
			throw new IllegalArgumentException("Cannot compare to null node");
		}
		if(distance < other.distance ){
			return -1;
		}
		if(distance > other.distance){
			return 1;
		}
		return 0;
	}

	public boolean isAdjacent(Node other){
		if(this == other){
			return true;
		}
		return adjacentNodes.contains(other);
	}
	
	public HashSet<Node> getAdjacentNodes(){
		return adjacentNodes;
	}

	public int id(){
		return id;
	}

	public int getDegree(){
		return adjacentNodes.size();
	}

	public String toString(){
		return "(" + id + ")";
	}

	@Override
	public int compareTo(Node other) {
		if(id < ((Node) other).id()){
			return -1;
		}
		if(id > ((Node) other).id()){
			return 1;
		}
		return 0;
	}
}
