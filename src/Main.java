import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
	
	public static void main(String[] args) {
		System.out.println();
		System.out.println("#######################");
		System.out.println("Paris January");
		System.out.println("#######################");
		System.out.println();
		for (int i = 1; i < 32; i++) {
			System.out.println();
			System.out.println();
			System.out.println("Building from a new day...");
			System.out.println();
			System.out.println();
			Build build = new Build();
			build.building("data/ParisSearchJan/Paris-2015-1-" + i);			
		}
		
		System.out.println();
		System.out.println("#######################");
		System.out.println("Paris February");
		System.out.println("#######################");
		System.out.println();
		for (int i = 1; i < 29; i++) {
			System.out.println();
			System.out.println();
			System.out.println("Building from a new day...");
			System.out.println();
			System.out.println();
			Build build = new Build();
			build.building("data/ParisSearchFeb/Paris-2015-2-" + i);			
		}
		
		System.out.println();
		System.out.println("#######################");
		System.out.println("Oscars");
		System.out.println("#######################");
		System.out.println();
		for (int i = 23; i < 27; i++) {
			System.out.println();
			System.out.println();
			System.out.println("Building from a new day...");
			System.out.println();
			System.out.println();
			Build build = new Build();
			build.building("data/Oscars/Oscars-2015-2-" + i);			
		}
		
		System.out.println();
		System.out.println("#######################");
		System.out.println("New York");
		System.out.println("#######################");
		System.out.println();
		for (int i = 23; i < 27; i++) {
			System.out.println();
			System.out.println();
			System.out.println("Building from a new day...");
			System.out.println();
			System.out.println();
			Build build = new Build();
			build.building("data/NewYorkOneWeek/NewYork-2015-2-" + i + ".txt");			
		}
	}
	
}

class  Build {
	public ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
	public static ArrayList<Tweet> allTweetList = new ArrayList<Tweet>();
	   
	Build () {
		tweetList = new ArrayList<Tweet>();
	}
	
	public Graph buildGraph() {
		return new Graph();
	}
	
	public Boolean checkTweet(Tweet tweet) {
		for (int i = 0; i < tweetList.size(); i++) {
			if (tweetList.get(i).getSize() == tweet.getSize()) {
				Boolean everythingIncluded = true;
				for (int j = 0; j < tweet.getSize(); j++) {
					Boolean included = false;
					for (int k = 0; k < tweetList.get(i).getSize(); k++) {
						if (tweetList.get(i).getList().get(k).equals(tweet.getList().get(j))) 
							included = true;
					}
					if (!included) {
						everythingIncluded = false;
						break;						
					}
					everythingIncluded &= included;
				}
				if (everythingIncluded) 
					return true;
			}
		}
		return false;
	}
	
	public void building(String s) {
		Graph graph = new Graph();
		
		
		System.out.println("Parsing...");
		Parser parser = new Parser(s);
		File file = new File(parser.fileName);
		System.out.println(parser.fileName);
		if (!file.exists()) {
			System.out.println("Doesn't exsist");
			return;
		}
		
		parser.getHashtags();
    	
		for (Tweet tweet : parser.tweetList) {
			allTweetList.add(tweet);
    		if (!checkTweet(tweet)) {
	        	tweetList.add(tweet);	        		
        	}
    	}
	      
        for (int i = 0; i < tweetList.size(); i++) {
        	graph.addTweet(tweetList.get(i));
        }
		
		GraphInfo graphInfo = new GraphInfo(graph);
		graphInfo.buildGraphInfo();
		
		GraphInfo result = graphInfo.extractSubgraph(0.1);
		result.printEverything(false);
		result.writeResultsInFile("data/Results/datasetname.subgr", false, s);
		
		GraphInfo resultGreedy = graphInfo.extractGreedySubgraph();
		resultGreedy.printEverything(true);
		resultGreedy.writeResultsInFile("data/Results/datasetname.subgr", true, s);
		//ArrayList<Graph> graphList = resultGreedy.convexSubgraphs();
		//for (Graph graphoumine : graphList)
		//	graphoumine.printEverything();
	}
	
	public static void printNumberOfUsers(GraphInfo graphInfo) {
		int number = 0;
		ArrayList<Long> list = new ArrayList<Long>();
		for (int i = 0; i < graphInfo.nodes.size(); i++) {
			if (graphInfo.nodes.get(i)) {
				for (Tweet tweet : allTweetList) {
					if (tweet.containsHashtag(graphInfo.graph.nodes.get(i).getHash())) {
						//Just to check if the user ids are all true
						if (tweet.getUserId() == 0l) 
							System.out.println("Issue, 0 as user id");
						//System.out.println("User: " + tweet.getUserId());
						if (!list.contains(tweet.getUserId()))
							list.add(tweet.getUserId());
						number++;
					}
				}
			}
		}
		System.out.println("Number of tweets for this subgraph: " + number);
		System.out.println("Number of different users for this subgraph :" + list.size());
		System.out.println();
	}
	
	public static ArrayList<Integer> numberOfUser(GraphInfo graphInfo) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int number = 0;
		ArrayList<Long> list = new ArrayList<Long>();
		for (int i = 0; i < graphInfo.nodes.size(); i++) {
			if (graphInfo.nodes.get(i)) {
				for (Tweet tweet : allTweetList) {
					if (tweet.containsHashtag(graphInfo.graph.nodes.get(i).getHash())) {
						//Just to check if the user ids are all true
						if (tweet.getUserId() == 0l) 
							System.out.println("Issue, 0 as user id");
						//System.out.println("User: " + tweet.getUserId());
						if (!list.contains(tweet.getUserId()))
							list.add(tweet.getUserId());
						number++;
					}
				}
			}
		}
		result.add(number);
		result.add(list.size());
		return result;
	}
}
