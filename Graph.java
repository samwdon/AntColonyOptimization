package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Random;

public class Graph {
	
	private ArrayList<Node> nodes;
	private PriorityQueue<Edge> edges;
	private int size;
	private int numEdges;
	private Node start;
	private Node end;
	
	/**
	 * Generates a {@link Graph} from the provided nodes and edges.
	 * @param nodes
	 * @param edges
	 * @param start
	 * @param end
	 */
	public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges, Node start, Node end){
		this.start = start;
		this.end = end;
		this.nodes = nodes;
		this.size = nodes.size();
		numEdges = edges.size();
		this.edges = new PriorityQueue<Edge>(size);		
		for(Edge e : edges){
			if(!( nodes.contains(e.to()) && nodes.contains(e.from()) )){
				throw new IllegalArgumentException("Edges contained a node that was not supplied");
			}
			this.edges.add(e);
		}
	}
	
	/**
	 * Randomly generates a connected {@link Graph} with specified number of nodes and edges.
	 * @param size - number of nodes
	 * @param sparseness - number of edges. Between |N|-1 and |N||N-1|/2
	 */
	public Graph(int size, int sparseness){
		this.size = size;
		numEdges = 0;
		edges = new PriorityQueue<Edge>(size);		
		nodes = new ArrayList<Node>();
		Random random = new Random();
		
		if(sparseness < size - 1 || sparseness > size * (size -1)/2){
			throw new IllegalArgumentException("Sparseness must be between |N|-1 and |N||N-1|/2");
		}		
		
		for( int i = 0; i < size; i++){
			Node nextNode = new Node();
			nodes.add(nextNode);
			if(i == 0){
				start = nextNode;
			}	
			else{
				Node randPreviousNode = nodes.get( random.nextInt(i) );
				nextNode.connect(randPreviousNode);
				edges.add(new Edge(nextNode, randPreviousNode));
				numEdges++;
			}
		}
		this.end = nodes.get(size - 1);
		
		int rand1 = random.nextInt(size);
		int rand2 = random.nextInt(size);
		
		while(numEdges < sparseness){
			
			Node a = nodes.get(rand1);
			Node b = nodes.get(rand2);
			
			while(a.isAdjacent(b)){
				rand1 = random.nextInt(size);
				rand2 = random.nextInt(size);
				a = nodes.get(rand1);
				b = nodes.get(rand2);
			}
			
			a.connect(b);
			edges.add(new Edge(a,b));
			numEdges++;	
		}
	}
	
	/**
	 * Dijkstras shortest path algorithm to compare heuristic solution to the analytical
	 * @return the shortest path
	 */
	public ArrayList<Edge> dijkstras(){
		
		for(Node n : nodes){
			if(n == start){
				n.distance = 0;
			}
			else{
				n.distance = Integer.MAX_VALUE;
			}
		}

		Hashtable<Node,Edge> parents = new Hashtable<Node,Edge>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(nodes.size(),
				new Comparator<Node>( ) {
					// overriding the compare method
					public int compare(Node n, Node m) {
						return n.compareDistances(m);
					}
				}
		  );
		for(Node n: nodes){
			queue.add(n);
		}
		while(!queue.isEmpty()){
			Node current = queue.poll();
			if( current.distance == Integer.MAX_VALUE){
				break;	//if this happens, none of the remaining nodes are accessible from the start
			}
			for(Edge neighborEdge : this.getEdgesConnectedTo(current)){
				
				Node neighbor = neighborEdge.oppositeEnd(current);
				int altDistance = current.distance + 1;
				
				if(current.distance == Integer.MAX_VALUE){		//prevent integer wrap around
					altDistance = Integer.MAX_VALUE;
				}
				
				//Relaxation step
				if(altDistance < neighbor.distance){
					//must remove neighbor and add again to update its position now that its distance has changed
					queue.remove(neighbor);
					neighbor.distance = altDistance;
					queue.add(neighbor);
					parents.put(neighbor, neighborEdge);	//update the parent of neighbor to the current node
				}
			}
		}
		
		Edge curEdge = parents.get(end);
		Node curNode = end;
		ArrayList<Edge> path = new ArrayList<Edge>();
		while(curEdge.from() != start && curEdge.to() != start){
			path.add(curEdge);
			curNode = curEdge.oppositeEnd(curNode);
			curEdge = parents.get(curNode);
		}
		path.add(curEdge);
		Collections.reverse(path);
		return path;
	}
	
	/**
	 * lowers the pheromone level of every edge in the graph to simulate evaporation over time
	 */
	public void evaporate() {
		for(Edge e : edges){
			e.lowerPheremoneLevel();
		}
	}
	
	public String toString(){
		String output = "";
		int prevFromId = 0;
		PriorityQueue<Edge> copyOfEdges = new PriorityQueue<Edge>();
		for(Edge e : edges){
			copyOfEdges.add(e);
		}
		while(!copyOfEdges.isEmpty()){
			Edge e = copyOfEdges.poll();
			if(e.from().id() != prevFromId){
				output += "\n";
				prevFromId = e.from().id();
			}
			output += e.toString() +" ";
		}
		return output;
	}
	
	public void removeEdge(Edge e){
		Node from = e.from();
		Node to = e.to();
		if( !from.isAdjacent(to) ){
			throw new IllegalArgumentException("Nodes " + from.id() +" and " + to.id() + " are not connected");
		}
		from.getAdjacentNodes().remove(to);
		to.getAdjacentNodes().remove(from);
		edges.remove(e);
	}
	
	public ArrayList<Edge> getEdgesConnectedTo(Node n){
		ArrayList<Edge> connected = new ArrayList<Edge>();
		for(Edge e : edges){
			if(e.from() == n || e.to() == n){
				connected.add(e);
			}
		}
		return connected;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public PriorityQueue<Edge> getEdges() {
		return edges;
	}

	public int getSize() {
		return size;
	}

	public double getNumberEdges() {
		return numEdges;
	}
	
	public Node getStart(){
		return start;
	}
	
	public Node getEnd(){
		return end;
	}

}
