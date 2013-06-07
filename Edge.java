package main;

public class Edge implements Comparable<Edge>{
	
	private Node from;
	private Node to;
	private static int numEdges = 0;
	private int id;
	private double pheromoneLevel = 1;
	private final static double PHEROMONE_DROP = .1;
	
	public Edge(Node from, Node to){
		if(from.compareTo(to) == 0){
			throw new IllegalArgumentException("Cannot have edge to the same Node");
		}
		if(from.compareTo(to) < 0){
			this.from = from;
			this.to = to;
		}
		else{
			this.from = to;
			this.to = from;
		}
		id = numEdges;
		numEdges++;
	}
	
	/**
	 * Raise the edge's pheromone level by the specified amount
	 * @return the new pheromone level
	 */
	public double raisePheremoneLevel(double amount){
		pheromoneLevel += amount;
		return pheromoneLevel;
	}
	
	/**
	 * Lower the edge's pheromone level by the specified amount to a minimum of 1
	 * @return the new pheromone level
	 */
	public double lowerPheremoneLevel(){
		pheromoneLevel -= PHEROMONE_DROP;
		if(pheromoneLevel < 1){
			pheromoneLevel = 1;
		}
		return pheromoneLevel;
	}
	
	/**
	 * @return the Node of the edge opposite of the supplied Node
	 */
	public Node oppositeEnd(Node n){
		if(from != n && to != n){
			throw new IllegalArgumentException("Edge " + this + " did not contain node " + n);
		}
		if(from == n){
			return to;
		}
		else{
			return from;
		}
	}
	
	public double getPheremoneLevel(){
		return pheromoneLevel;
	}

	public Node from() {
		return from;
	}

	public Node to() {
		return to;
	}

	public int id() {
		return id;
	}
	
	public boolean equals(Edge other){
		if(other == null){
			return false;
		}
		if(from == other.from() && to == other.to()){
			return true;
		}
		if(from == other.to() && to == other.from()){
			return true;
		}
		return false;
	}
	
	public String toString(){
		int lowerId = Math.min(from.id(), to.id());
		int higherId = Math.max(from.id(), to.id());
		return "<" + lowerId + ", " + higherId + "> ";
	}

	@Override
	public int compareTo(Edge other) {
		if(this.from().compareTo(((Edge) other).from()) < 0){
			return -1;
		}
		else if(this.from().compareTo(((Edge) other).from()) > 0){
			return 1;
		}
		else{
			if(this.to().compareTo(((Edge) other).to()) < 0){
				return -1;
			}
			if(this.to().compareTo(((Edge) other).to()) > 0){
				return 1;
			}
			return 0;
		}
	}
	

}
