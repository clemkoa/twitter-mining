public class SparseEdge {
	private int node;
	private int weight;
	
	SparseEdge(int node, int weight) {
		this.node = node;
		this.weight = weight;
	}
	
	SparseEdge(SparseEdge edge) {
		this(edge.node, edge.weight);
	}
	
	public int getNode() {
		return node;
	}
	
	public void setNode(int i) {
		node = i;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWieght(int w) {
		weight = w;
	}
}
