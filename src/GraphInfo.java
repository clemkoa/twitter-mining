import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class GraphInfo {
	ArrayList<Boolean> nodes = new ArrayList<Boolean>();
	ArrayList<ArrayList<SparseEdge>> accessibleNodes = new ArrayList<ArrayList<SparseEdge>>();
	Graph graph;
	
	GraphInfo(Graph graph) {
		this.graph = graph;
	}
	
	GraphInfo(GraphInfo graphInfo) {
		for (Boolean bool : graphInfo.nodes) {
			nodes.add(bool);
		}
		
		for (int i = 0; i < graphInfo.accessibleNodes.size(); i++) {
			accessibleNodes.add(new ArrayList<SparseEdge>());
			for (int j = 0; j < graphInfo.accessibleNodes.get(i).size(); j++) {
				accessibleNodes.get(i).add(new SparseEdge(graphInfo.accessibleNodes.get(i).get(j)));
			}
		}
		
		graph = new Graph(graphInfo.graph);
	}
	
	public void buildGraphInfo() {
		System.out.println("Building graphInfo");
		for (Node node : graph.nodes) {
			nodes.add(true);
			accessibleNodes.add(new ArrayList<SparseEdge>());
		}
		
		for (Edge edge : graph.edges) {
			if (!accessibleNodes.get(edge.getStart()).contains(edge.getEnd()))
				accessibleNodes.get(edge.getStart()).add(new SparseEdge(edge.getEnd(), edge.getWeight()));
			if (!accessibleNodes.get(edge.getEnd()).contains(edge.getStart()))
				accessibleNodes.get(edge.getEnd()).add(new SparseEdge(edge.getStart(), edge.getWeight()));
		}
	}
	
	/*public void extractGreedySubgraph() {
		Boolean finished = false;
		int iteration = 0;
		System.out.println("Starting greedy algorithm");
		
		ArrayList<Boolean> temporaryNodes = nodes;
		ArrayList<ArrayList<SparseEdge>> temporaryAccessibleNodes = accessibleNodes;
		
		while (!finished) {
			iteration++;
			double density = getTemporaryDensity(temporaryNodes, temporaryAccessibleNodes);
			
			int id = getMinId(temporaryNodes, temporaryAccessibleNodes);
			removeNode(id, temporaryAccessibleNodes,temporaryNodes);
			
			double temporaryDensity = getTemporaryDensity(temporaryNodes, temporaryAccessibleNodes);
			if (temporaryNodes.size() <= 2)
				finished = true;
			if (temporaryDensity > density) {
				nodes = temporaryNodes;
				accessibleNodes = temporaryAccessibleNodes;
			} else if (temporaryNodes.size() < 11) 
				finished = true;
		}
		System.out.println("Greedy algorithm finished in " + iteration + " iterations");
	}*/
	
	public GraphInfo extractGreedySubgraph() {
		GraphInfo G = new GraphInfo(this);
		GraphInfo maxG = new GraphInfo(G);
		double maxDensity = maxG.getTemporaryDensity();
		
		while (G.notEmpty()) {
			int minId = G.getMinId();
			removeNode(minId, G.accessibleNodes, G.nodes);
			
			if (G.notEmpty() && G.getTemporaryDensity() > maxDensity) {
				maxG = new GraphInfo(G);
				maxDensity = G.getTemporaryDensity();			
			}
			if (G.equals(maxG))
				return maxG;
		}
		
		return maxG;
	}
	
	public GraphInfo extractSubgraph(double epsilon) {
		
		GraphInfo G = new GraphInfo(this);
		GraphInfo maxG = new GraphInfo(G);
		double maxDensity = maxG.getTemporaryDensity();
		
		while (G.notEmpty()) {
			double density = G.getTemporaryDensity();
			for (int i = 0; i < G.nodes.size(); i++) {
				if (G.nodes.get(i)) {
					if (getDegree(i, G.accessibleNodes, G.nodes) <= (2*density*((double) 1.0 + epsilon))) {
						removeNode(i,G.accessibleNodes, G.nodes);
					}
				}
			}
			
			if (G.notEmpty() && G.getTemporaryDensity() > maxDensity) {
				maxG = new GraphInfo(G);
				maxDensity = G.getTemporaryDensity();
			}
		}
		
		return maxG;
	}
	
	public double getTemporaryDensity() {
		double res = 0.0;
		int nodeSize = 0;
		
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i)) {
				res += getDegree(i, accessibleNodes, nodes);
				nodeSize++;
			}
		}
		
		return res/((double) 2*nodeSize);
	}
	
	public Boolean notEmpty() {
		Boolean notEmpty = false;
		for (Boolean bool : nodes) 
			if (bool)
				notEmpty = true;
		return notEmpty;
	}

	public int getDegree(int id,ArrayList<ArrayList<SparseEdge>> accessibleNodes, ArrayList<Boolean> nodes) {
		int res = 0;
		for (int i = 0; i < accessibleNodes.get(id).size(); i++) {
			res += accessibleNodes.get(id).get(i).getWeight();
		}
		return res;
	}
	
	public int nodeSize() {
		int res = 0;
		for (Boolean bool : nodes) {
			if (bool)
				res++;
		}
		return res;
	}
	
	public int size(ArrayList<Boolean> nodes) {
		int res = 0;
		for (Boolean bool : nodes) {
			if (bool)
				res++;
		}
		return res;
	}
	
	public void removeNode(int i, ArrayList<ArrayList<SparseEdge>> accessibleNodes, ArrayList<Boolean> nodes) {
		nodes.set(i, false);

		accessibleNodes.set(i, new ArrayList<SparseEdge>());
		
		for (ArrayList<SparseEdge> list : accessibleNodes) {
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getNode() == i) {
					list.remove(j);
					j--;
				}
			}
		}
	}
	
	public int getFirstValidId(ArrayList<Boolean> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i))
				return i;
		}
		System.out.println("no valid");
		return -1;
	}
	
	public int getMinId() {
		int minId = getFirstValidId(nodes);
		int min = getDegree(minId, accessibleNodes, nodes);
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i)) {
				if (getDegree(i, accessibleNodes, nodes) <= min) {
					minId = i;
					min = getDegree(i, accessibleNodes, nodes);
				}
			}
		}
		return minId;
	}
	
	public ArrayList<Graph> convexSubgraphs() {
		ArrayList<Graph> graphList = new ArrayList<Graph>();
		ArrayList<Integer> visitedNodes = new ArrayList<Integer>();
	
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i)) {
				if (!visitedNodes.contains(i)) {
					visitedNodes.add(i);
					Graph graph = new Graph();
					ArrayList<String> hashtagList = new ArrayList<String>();
					graph.addNode(graph.nodes.get(i).getHash());
					for (int j = 0; j < accessibleNodes.get(i).size(); j++) {
						hashtagList.add(graph.nodes.get(accessibleNodes.get(i).get(j).getNode()).getHash());
					}
					Tweet tweet = new Tweet(hashtagList);
					graph.addTweet(tweet);
					graphList.add(graph);
				
				}
			}
		}
		
		return graphList;
	}
	
	public void printEverything(boolean greedy) {
		System.out.println("GraphInfo");
		if (greedy) 
			System.out.println("Greedy algorithm result: ");
		else 
			System.out.println("Linear algorithm result: ");
		System.out.println();
		
		System.out.println("Densest subgraph:");
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i)) {
				System.out.println("#" + graph.nodes.get(i).getHash());
			}
		}
		
		/*for (int i = 0; i < accessibleNodes.size(); i++) {
			System.out.println("Node " + i + " going to:");
			for (Integer j : accessibleNodes.get(i)) {
				System.out.print(" " + j + " ");
			}
			System.out.println();
		}*/
		
		System.out.println("Number of nodes: " + nodeSize());
		System.out.println("Density : " + getTemporaryDensity());
		Build.printNumberOfUsers(this);
	}
	
	public void writeResultsInFile(String fileName, boolean greedy, String originalFile) {
		try {
			File file = new File(fileName);
			if (file.createNewFile()) {
			}
			
			FileWriter fw = new FileWriter(fileName,true);
			
			fw.write("GraphInfo for file: " + originalFile + "\n");
			if (greedy) 
				fw.write("(Greedy algorithm) ");
			else 
				fw.write("(Parallel algorithm) ");
			
			fw.write("Concerned hashtags: ");
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i)) {
					fw.write("#" + graph.nodes.get(i).getHash() + " ");
				}
			}
			
			fw.write("\nThe densest subgraph has " + nodeSize() + " nodes, ");
			fw.write("a density of " + getTemporaryDensity() + ". It involved ");
			ArrayList<Integer> list = Build.numberOfUser(this);
			fw.write(list.get(0) + " tweets among " + list.get(1) + " users.\n");
			fw.close();
		}catch (IOException e) {
		}
	}
}