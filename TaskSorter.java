import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/***
 * 
 * This program uses a DirectedGraph object to determine whether or not there are tasks that are mutually dependent. 
 * If no tasks are co-dependent, the program will then output a numbered list which sorts the tasks into a valid order.
 * If any tasks are co-dependent, then they must be done at the same time. In this case, some numbered items will 
 * be comma-separated lists.
 * 
 * @author Colin Monaghan
 * @version 12.10.2020
 */
public class TaskSorter {

	/**
	 * This method reads a specified file containing n lines of n different tasks. 
	 * Each of the file line must be a tab-separated list, where the first token 
	 * is some new task, and all remaining tokens are the other tasks that must 
	 * be done prior to it. For example, this line defines task A, but indicates 
	 * that tasks B and C must be done first: "A	B	C".
	 * This method adds each task to the arrayList of tasks and graph.
	 * @param filename the inputed file
	 * @param graph the DirectedGraph for the tasks
	 * @param tasks the ArrayList of tasks
	 */
	private static void fileReader(String filename, DirectedGraph graph, ArrayList<String> tasks) {
		try {
			File file = new File(filename);
			//create a scanner to read the file
			Scanner scanner = new Scanner(file);
			//a string to hold each line of the file
			String line;
			
			//go through each line of the file
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				// split the line apart by tabs
				String[] tokens = line.split("\t");
				//add each task on the file to the graph
				for(int i = 0; i < tokens.length; i++) {
					
					//if the task does not exist as a vertex, add it as a vertex
					if(!tasks.contains(tokens[i])) {
						//add the string to the tasks and graph
						tasks.add(tokens[i]);
						graph.addVertex(tasks.size()-1);
					}
					
					if(i > 0) {
						//if we are not the first task, then the first task is dependent on this task
						//so add an edge from the this task to the first task (with weight 1.0 since 
						//we don't care about the weight in this case)
						graph.addEdge(tasks.indexOf(tokens[i]), tasks.indexOf(tokens[0]), 1.0);
					}
				}
			}
			
			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method checks if an inputed DirectedGraph contains cycles. If a cycle 
	 * exists, then the method returns true. Otherwise, it returns false.
	 * @param graph the DirectedGraph we are checking
	 * @return true if the graph contains cycles
	 */
	public static boolean containsCycle(DirectedGraph graph) {
		  // allocation
		  boolean[] marked = new boolean[graph.countVertices()];
		  boolean[] onStack = new boolean[graph.countVertices()];
		  // look for cycles, 1 component at a time
		  for (int v = 0; v < graph.countVertices(); v++) {
			  if(!marked[v] && cycleFindingDFT(graph, v, marked, onStack))
				  return true;
		  }
		  // if no cycles, return false
		  return false;
	}
	
	/**
	 * This method recursively determines if the inputed graph has a cycle. 
	 * It will check all the adjacent vertices of the current vertex to see 
	 * if any of them lead back to a vertex we have already visited.
	 * @param graph the DirectedGraph
	 * @param vertex the current vertex we are exploring
	 * @param marked an array of booleans that mark each vertex as true if it has been explored
	 * @param onStack an array of booleans that mark each vertex as true if it is currently being explored
	 * @return
	 */
	private static boolean cycleFindingDFT(DirectedGraph graph, int vertex, boolean[] marked, boolean[] onStack) {
		  // mark the element off & add it to the stack
		  marked[vertex] = true;
		  onStack[vertex] = true;
		  // see if any neighbors are on the stack, explore
		  for (int v : graph.getAdjacencyList(vertex)) {
			  if (onStack[v]) return true;
			  if (!marked[v]) {
				  if (cycleFindingDFT(graph, v, marked, onStack))
					  return true;
			  }
		  }
		  // if we get here, we only found dead ends
		  onStack[vertex] = false;
		  return false;
		}
	
	/**
	 * This method prints an ordering of mutually dependent tasks (there is a 
	 * cycle in the graph) on the inputed graph. If two tasks are mutually dependent, 
	 * they are printed out together since they must be done at the same time.
	 * @param filename the name of the file
	 * @param graph the DirectedGraph object
	 * @param tasks the arrayList of tasks
	 */
	private static void printCyclicOrdering(String filename, DirectedGraph graph, ArrayList<String> tasks) {
		System.out.println("The file \""+filename+"\" contains mutually dependent tasks. You must:");
		//get an order for the graph 
		int[] order = sortTopologically(graph.reverse());
		boolean[] marked = new boolean[graph.countVertices()];
		ArrayList<String> components = new ArrayList<>();
		//group each of the components into a string 
		for(int i = 0; i < order.length; i++) {
			if(!marked[order[i]]) {
				StringBuilder sb = new StringBuilder();
				components.add(stringOfComponent(graph, tasks, order[i], marked, sb));
			}
			
		}
		//print out the ordering
		for(int i = 1; i <= components.size(); i++) {
			System.out.println("\t "+i+". "+components.get(components.size() - i) );
		}
	}
	
	/**
	 * This method recursively builds a string representing all the tasks in a component 
	 * on the graph (the component of the inputed vertex). After all the vertices of the component 
	 * are traversed, the method returns the string of tasks.
	 * @param graph the DirectedGraph object
	 * @param tasks the arrayList of tasks
	 * @param vertex the current vertex being explored
	 * @param marked an array of booleans that mark each vertex as true if it has been explored
	 * @param sb a stringbuilder that builds a string representing all the tasks in the component
	 * @return a string representing all the tasks in the component 
	 */
	private static String stringOfComponent(DirectedGraph graph, ArrayList<String> tasks, int vertex, boolean[] marked, StringBuilder sb) {
		  // mark the element
		  marked[vertex] = true;
		  sb.append(tasks.get(vertex));

		  // explore
		  for (int v : graph.getAdjacencyList(vertex)) {
			  if (!marked[v]) {
				  sb.append(", ");
				  stringOfComponent(graph, tasks, v, marked, sb);
			  }
		  }
		  return sb.toString();
		}
	
	/**
	 * This method prints an ordering of mutually independent tasks (there is not a
	 * cycle in the graph) on the inputed graph. An ordering of the tasks is printed out.
	 * @param filename the name of the file
	 * @param graph the DirectedGraph object
	 * @param tasks the arrayList of tasks
	 */
	private static void printAcyclicOrdering(String filename, DirectedGraph graph, ArrayList<String> tasks) {
		System.out.println("The file \""+filename+"\" contains no mutually dependent tasks. You must:");
		//get an order for the graph 
		int[] order = sortTopologically(graph);
		//print out the ordering
		for(int i = 0; i < order.length ; i++) {
			System.out.println("\t"+(i+1)+". "+tasks.get(order[i]));
		}
	}
	
	/**
	 * This method determines and returns a topological ordering for an acyclic DirectedGraph. The
	 * topological ordering is a reverse post-order list of the inputed graph. The 
	 * ordering is found through a Depth-First Traversal through the vertices on the graph.
	 * @param graph the DirectedGraph object
	 * @return a topological ordering for a cyclic DirectedGraph
	 */
	private static int[] sortTopologically(DirectedGraph graph) {
		  // allocation
		  boolean[] marked = new boolean[graph.countVertices()];
		  int[] list = new int[graph.countVertices()];
		  int[] index = new int[1];
		  index[0] = list.length-1;
		  // traverse
		  for (int v = 0; v < graph.countVertices(); v++) {
			  if (marked[v]) continue;
			  topoSortDFT(graph, v, marked, list, index);
		  }
		  // return the list
		  return list;
		}
	
	/**
	 * This method recursively determines the topological ordering of the graph using a Depth-First Traversal. 
	 * Whenever a vertex is explored, it is marked on the marked array. When a vertex has no more adjacent 
	 * vertices to explore, it is added to the end of the list.
	 * @param graph the DirectedGraph
	 * @param vertex the current vertex we are exploring
	 * @param marked an array of booleans that mark each vertex as true if it has been explored
	 * @param list the list of the vertices in topological order
	 * @param index our current index in the list array (since we are filling it backwards)
	 */
	private static void topoSortDFT(DirectedGraph graph, int vertex, boolean[] marked, int[] list, int[] index) {
		  // mark the element
		  marked[vertex] = true;
		  // explore
		  for (int v : graph.getAdjacencyList(vertex)) {
			  if (!marked[v]) {
				  topoSortDFT(graph, v, marked, list, index);
			  }
		  }
		  // at the end, add this vertex to the list
		  list[index[0]] = vertex;
		  index[0]--;
		}
	
	/**
	 * This is the main method of the TaskSorter Class. It takes a file of tasks as an input. 
	 * It creates a DirectedGraph object and an arrayList of tasks, then reads the file and fills the two objects with tasks. 
	 * Then it checks the graph for cycles. If it contains cycles, it will print out a cyclic ordering of the tasks. 
	 * Otherwise, it will print out the acyclic ordering.
	 * @param args the file name of the file containing all the tasks
	 */
	public static void main(String[] args) {
		// if arguments are empty, print a polite message and quit
		if (args.length != 1) {
			System.out.println("Invalid amount of arguments.");
			System.exit(0);
		}
		String filename = args[0];

		// Create the graph
		DirectedGraph graph = new DirectedGraph(1);

		//this maps a task to its vertex value on the graph
		ArrayList<String> tasks = new ArrayList<>();
		
		//read the file
		fileReader(filename, graph, tasks);
		
		//check if the graph contains cycles
		boolean hasCycles = containsCycle(graph);
		
		//if it has cycles, then find the cyclic ordering
		if(hasCycles) printCyclicOrdering(filename, graph, tasks);
		//if it has no cycles, then find the acyclic ordering
		else printAcyclicOrdering(filename, graph, tasks);
		
	}

}
