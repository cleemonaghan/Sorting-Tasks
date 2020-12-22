import java.util.ArrayList;
import java.util.HashMap;

/***
 * This directed graph object represents vertices and edges on a directed graph 
 * using a hashMap that maps integers to ArrayLists of Edges.
 * @author Colin Monaghan
 * @version 12.10.2020
 */
public class DirectedGraph {

	//this graph objects maps a vertex to an arrayList of its edges
	private HashMap<Integer, ArrayList<Edge>> graph;
	//the number of edges on the graph
	private int numEdges;

	
	/**
	 * This is a constructor for the graph that creates an empty graph.
	 */
	public DirectedGraph() {
		graph = new HashMap<>();
		numEdges = 0;
	}
	
	/**
	 * This is a constructor for the graph that creates an empty graph 
	 * with a specified initial capacity of the number of vertices on the graph.
	 */
	public DirectedGraph(int numVertices) {
		graph = new HashMap<>(numVertices);
		numEdges = 0;
	}

	/**
	 * This method returns the number of vertices on the graph.
	 * @return the number of vertices on the graph
	 */
	public int countVertices() {
		return graph.size();
	}
	

	/**
	 * This method returns the number of edges on the graph.
	 * @return the number of edges on the graph
	 */
	public int countEdges() {
		return numEdges;
	}

	/**
	 * This method adds an edge to the graph from vertex 1 to vertex 2.
	 * @param v1 the start vertex of the edge 
	 * @param v2 the finish vertex of the edge
	 * @param weight the length of the edge
	 */
	public void addEdge(int v1, int v2, double weight) {
		//create the new edge
		Edge newEdge = new Edge(v1, v2, weight);
		//add the new edge to the graph
		graph.get(v1).add(newEdge);
		numEdges++;
	}

	/**
	 * This method adds an edge to the graph
	 * @param edge the edge to add to the graph
	 */
	public void addEdge(Edge edge) {
		//get the the arrayList of edges for the start vertex of 
		//the new edge, and add this new edge to that arrayList
		graph.get(edge.getStart()).add(edge);
		numEdges++;
	}

	/**
	 * This method adds a vertex to the graph.
	 * @param vertex the vertex to add to the graph
	 */
	public void addVertex(int vertex) {
		// add new vertex to the HashMap
		graph.put(vertex, new ArrayList<Edge>());
	}

	/**
	 * This method removes an edge from the graph.
	 * @param v1 the start vertex of the edge
	 * @param v2 the end vertex of the edge
	 */
	public void deleteEdge(int v1, int v2) {
		//remove the edge from the graph
		ArrayList<Edge> array = graph.get(v1);
		int i = 0;
		boolean found = false;
		while(i < array.size() && !found ) {
			//look through the arrayList of edges and 
			//see if there is an edge with the correct end vertex
			if(array.get(i).getEnd() == v2) {
				array.remove(i);
				found = true;
			}
			i++;
		}
		numEdges--;
	}

	/**
	 * This method removes a vertex from the graph.
	 * @param vertex the vertex to remove
	 */
	public void deleteVertex(int vertex) {
		// kill a vertex
		graph.remove(vertex);
	}

	/**
	 * This method tests if two vertices are adjacent to one 
	 * another (ie. vertex one is connected to vertex two by an edge).
	 * @param v1 the first vertex
	 * @param v2 the second vertex
	 * @return true if the two vertices are adjacent
	 */
	public boolean areAdjacent(int v1, int v2) {
		ArrayList<Edge> array = graph.get(v1);
		int i = 0;
		while(i < array.size()) {
			//look through the arrayList of edges and 
			//see if there is an edge with the correct end vertex
			if(array.get(i).getEnd() == v2) {
				return true;
			}
			i++;
		}
		return false;
	}

	/**
	 * This method returns an array of all the vertices that a given vertex is connected to.
	 * @param vertex the vertex we are investigating
	 * @return an array of the vertice's adjacent vertices
	 */
	public int[] getAdjacencyList(int vertex) {
		ArrayList<Edge> edges = graph.get(vertex);
		int[] array = new int[edges.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = edges.get(i).getEnd();
		}
		return array;
	}
	
	/**
	 * This method returns a new graph identical 
	 * to this one, but with reversed edges.
	 * @return a reversed DirectedGraph
	 */
	public DirectedGraph reverse() {
		//make a new graph with the same size as this one
		DirectedGraph newGraph = new DirectedGraph(this.countVertices());
		
		//add all the vertices to the new graph
		for(int i = 0; i < this.countVertices(); i++) {
			newGraph.addVertex(i);
		}
		
		//go through this graph and adding the reverse of each edge to the new graph 
		for(int i = 0; i < this.countVertices(); i++) {
			for(Edge edge: this.graph.get(i)) {
				newGraph.addEdge(edge.getEnd(), edge.getStart(), edge.getWeight());
			}
		}
		return newGraph;
	}

	
	/***
	 * This class represents edges between vertices on the graph. Every Edge has two
	 * vertices and a weight.
	 * 
	 * @author Colin Monaghan
	 *
	 */
	protected class Edge implements Comparable<Edge> {
		private int start;
		private int end;
		private double weight;

		/**
		 * This method creates an edge from the start vertex to 
		 * the end vertex with a specified weight.
		 * 
		 * @param v1     the start vertex
		 * @param v2     the end vertex
		 * @param weight the distance between the two vertices
		 */
		public Edge(int v1, int v2, double weight) {
			this.start = v1;
			this.end = v2;
			this.weight = weight;
		}

		/**
		 * This method returns the start vertex of the edge
		 * 
		 * @return the start vertex of the edge
		 */
		public int getStart() {
			return this.start;
		}

		/**
		 * This method returns the end vertex of the edge
		 * 
		 * @return the end vertex of the edge
		 */
		public int getEnd() {
			return this.end;
		}
		
		/**
		 * This method returns the weight of the edge
		 * 
		 * @return the weight of the edge
		 */
		public double getWeight() {
			return this.weight;
		}

		/**
		 * This method compares this edge to another edge.
		 * 
		 * @param other the other edge we are comparing this edge to
		 * @return whether this edge is greater than, less than or equal to the other
		 *         edge (1, -1 and 0 respectively)
		 */
		public int compareTo(Edge other) {
			if (other.getWeight() < this.weight)
				return -1;
			else if (other.getWeight() > this.weight)
				return 1;
			else
				return 0;
		}
	}
}
