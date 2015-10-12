import java.util.ArrayList;


public class Graph {
	ArrayList<Node> nodes;
	ArrayList<Edge> edges;
	
	Graph(Graph graph) {
		this.nodes = graph.nodes;
		this.edges = graph.edges;
	}
	
	Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}
	
	Graph() {
		this(new ArrayList<Node>(), new ArrayList<Edge>());
	}
	
	public int addNode(String hashtag) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getHash().equals(hashtag)) {
				nodes.get(i).incrementOccurence();
				return i;
			}
		}
		
		nodes.add(new Node(nodes.size(), hashtag));
		return nodes.size()-1;
	}
	
	public void addEdge(Edge edge) {
		for (int i = 0; i < edges.size(); i++) {
			if (edge.getStart() == edges.get(i).getStart() && edge.getEnd() == edges.get(i).getEnd()) {
				edges.get(i).incrementWeight();
				return;
			} else if (edge.getStart() == edges.get(i).getEnd() && edge.getEnd() == edges.get(i).getStart()) {
				edges.get(i).incrementWeight();
				return;
			}
		}
		//increment degree form nodes
		incrementDegreeFromNode(edge.getStart());
		incrementDegreeFromNode(edge.getEnd());
		
		//add edges
		edges.add(edge);
	}
	
	public void addTweet(Tweet tweet) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		//add the nodes first and keep the ids in mind
		for (int i = 0; i < tweet.getList().size(); i++) {
			list.add(addNode(tweet.getList().get(i)));
		}
		
		//add edges
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				addEdge(new Edge(list.get(i),list.get(j)));
			}
		}
	}
	
	public void decrementEdges(int i, ArrayList<Edge> edges) {
		for (Edge edge : edges) 
			edge.decrementEdge(i);
	}
	
	public void printEverything() {
		System.out.println("Graph: ");
		System.out.println("");
		
		System.out.println("Nodes: ");
		for (int i = 0; i < nodes.size(); i++) {
			System.out.println("Hashtag: " + nodes.get(i).getHash() + " -- " + nodes.get(i).getId() + " // degree: " + nodes.get(i).getDegree());
		}
		/*
		System.out.println("Edges: ");
		for (int i = 0; i < edges.size(); i++) {
			System.out.println("Start: " + edges.get(i).getStart() + ", end: " + edges.get(i).getEnd());
		}*/
		
		System.out.println("Number of edges :" + edges.size());
		System.out.println("Number of nodes :" + nodes.size());
		System.out.println("Average degree: " + averageDegree());
		System.out.println("Density: " + density());
		System.out.println("");


	}
	
	public void incrementDegreeFromNode(int id) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getId() == id) {
				nodes.get(i).incrementDegree();
				return;
			}
		}
	}
	
	public void computeDegree() {
		for (Node node : nodes) {
			node.setDegree(0);
		}
		for (Node node : nodes) {
			for (Edge edge : edges) {
				if (edge.getStart() == node.getId() || edge.getEnd() == node.getId()) {
					node.incrementDegree();
				}
			}
		}
	}
	
	public double averageDegree() {
		computeDegree();
		int sum = 0;
		
		for (int i = 0; i < nodes.size(); i++) {
			sum += nodes.get(i).getDegree();
		}
		
		double res = ((double ) sum)/((double) nodes.size());
		
		return res;
	}
	
	public double density() {
		return ((double) edges.size()/((double) nodes.size()));
	}

}

class Node {
	private int id;
	private String hash;
	private int occurence;
	private int degree;

	Node (int id, int occurence, String hash, int degree) {
		this.id = id;
		this.occurence = occurence;
		this.hash = hash;
		this.degree = degree;
	}
	
	Node (int id, int occurence, String hash) {
		this(id, occurence, hash, 0);
	}
	
	Node (int id, String stringId) {
		this(id, 1, stringId);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int i) {
		id = i;
	}
	
	public int getOccurence() {
		return occurence;
	}
	
	public void setOccurence(int o) {
		occurence = o;
	}
	
	public void incrementOccurence() {
		occurence++;
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}	
	
	public int getDegree() {
		return degree;
	}
	
	public void setDegree(int d) {
		degree = d;
	}
	
	public void incrementDegree() {
		degree++;
	}
	
	public void increaseDegree(int i) {
		degree += i;
	}
	
	public void decrementDegree() {
		degree--;
	}
}

class Edge {
	private int start;
	private int end;
	private int weight;

	Edge (int start, int end, int weight) {
		this.start = start;
		this.end = end;
		this.weight = weight;
	}

	Edge (int start, int end) {
		this.start = start;
		this.end = end;
		this.weight = 1;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
	
	public int getWeight() {
		return weight;
	}

	public void setStart(int s) {
		start = s;
	}

	public void setEnd(int e) {
		end = e;
	}

	public void setWeight(int w) {
		weight = w;
	}
	
	public void incrementWeight() {
		weight++;
	}
	
	public void decrementEdge(int i) {
		if (start >= i) 
			start--;
		if (end >= i) 
			end--;
	}
	
	public int getOtherSide(int i) {
		if (start == i) 
			return end;
		else if (end == i)
			return start;
		else 
			return -1;
	}
}
