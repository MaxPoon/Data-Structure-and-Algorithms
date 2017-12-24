import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.*;

public class Evacuation {
	private static FastScanner in;

	public static void main(String[] args) throws IOException {
		in = new FastScanner();

		FlowGraph graph = readGraph();
		System.out.println(maxFlow(graph));
	}

	private static int maxFlow(FlowGraph graph) {
		int flow = 0;
		while(true){
			boolean foundPath = false;
			Queue<Integer> queue = new LinkedList<Integer>();
			int[] parentIds = new int[graph.size()];
			for(int i=0;i<parentIds.length;i++) parentIds[i]=-1;
			queue.add(0);
			//bfs
			while(!queue.isEmpty() && !foundPath){
				int node = queue.poll();
				List<Integer> ids = graph.getIds(node);
				for(int id: ids){
					Edge edge = graph.getEdge(id);
					if(edge.flow<edge.capacity && parentIds[edge.to]==-1){
						if(edge.to==edge.from) continue;
						parentIds[edge.to] = id;
						if(edge.to==graph.size()-1){
							foundPath = true;
							break;
						}
						queue.add(edge.to);
					}
				}
			}
			if(!foundPath) break;
			//find the value of the flow
			int to = graph.size()-1;
			int minCap = -1;
			while(to!=0){
				int id = parentIds[to];
				Edge edge = graph.getEdge(id);
				if(minCap==-1 || (edge.capacity-edge.flow)<minCap) minCap = edge.capacity-edge.flow;
				to = edge.from;
			}
			to = graph.size()-1;
			while(to!=0){
				int id = parentIds[to];
				Edge edge = graph.getEdge(id);
				graph.addFlow(id, minCap);
				to = edge.from;
			}
		}
		//calculate the flow
		for(int id: graph.getIds(0)){
			Edge edge = graph.getEdge(id);
			flow += edge.flow;
		}
		return flow;
	}

	static FlowGraph readGraph() throws IOException {
		int vertex_count = in.nextInt();
		int edge_count = in.nextInt();
		FlowGraph graph = new FlowGraph(vertex_count);

		for (int i = 0; i < edge_count; ++i) {
			int from = in.nextInt() - 1, to = in.nextInt() - 1, capacity = in.nextInt();
			graph.addEdge(from, to, capacity);
		}
		return graph;
	}

	static class Edge {
		int from, to, capacity, flow;

		public Edge(int from, int to, int capacity) {
			this.from = from;
			this.to = to;
			this.capacity = capacity;
			this.flow = 0;
		}
	}

	/* This class implements a bit unusual scheme to store the graph edges, in order
	 * to retrieve the backward edge for a given edge quickly. */
	static class FlowGraph {
		/* List of all - forward and backward - edges */
		private List<Edge> edges;

		/* These adjacency lists store only indices of edges from the edges list */
		private List<Integer>[] graph;

		public FlowGraph(int n) {
			this.graph = (ArrayList<Integer>[])new ArrayList[n];
			for (int i = 0; i < n; ++i)
				this.graph[i] = new ArrayList<>();
			this.edges = new ArrayList<>();
		}

		public void addEdge(int from, int to, int capacity) {
			/* Note that we first append a forward edge and then a backward edge,
			 * so all forward edges are stored at even indices (starting from 0),
			 * whereas backward edges are stored at odd indices. */
			Edge forwardEdge = new Edge(from, to, capacity);
			Edge backwardEdge = new Edge(to, from, 0);
			graph[from].add(edges.size());
			edges.add(forwardEdge);
			graph[to].add(edges.size());
			edges.add(backwardEdge);
		}

		public int size() {
			return graph.length;
		}

		public List<Integer> getIds(int from) {
			return graph[from];
		}

		public Edge getEdge(int id) {
			return edges.get(id);
		}

		public void addFlow(int id, int flow) {
			/* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
			 * due to the described above scheme. On the other hand, when we have to get a "backward"
			 * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
			 * should be taken.
			 *
			 * It turns out that id ^ 1 works for both cases. Think this through! */
			edges.get(id).flow += flow;
			edges.get(id ^ 1).flow -= flow;
		}
	}

	static class FastScanner {
		private BufferedReader reader;
		private StringTokenizer tokenizer;

		public FastScanner() {
			reader = new BufferedReader(new InputStreamReader(System.in));
			tokenizer = null;
		}

		public String next() throws IOException {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				tokenizer = new StringTokenizer(reader.readLine());
			}
			return tokenizer.nextToken();
		}

		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}
}
