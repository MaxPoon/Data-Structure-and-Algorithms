import java.io.*;
import java.util.*;

public class AirlineCrews {
	private FastScanner in;
	private PrintWriter out;

	public static void main(String[] args) throws IOException {
		new AirlineCrews().solve();
	}

	public void solve() throws IOException {
		in = new FastScanner();
		out = new PrintWriter(new BufferedOutputStream(System.out));
		boolean[][] bipartiteGraph = readData();
		int[] matching = findMatching(bipartiteGraph);
		writeResponse(matching);
		out.close();
	}

	boolean[][] readData() throws IOException {
		int numLeft = in.nextInt();
		int numRight = in.nextInt();
		boolean[][] adjMatrix = new boolean[numLeft][numRight];
		for (int i = 0; i < numLeft; ++i)
			for (int j = 0; j < numRight; ++j)
				adjMatrix[i][j] = (in.nextInt() == 1);
		return adjMatrix;
	}

	private static void maxFlow(FlowGraph graph) {
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
	}

	static FlowGraph readGraph(boolean[][] bipartiteGraph) {
		int n = bipartiteGraph.length;
		int m = bipartiteGraph[0].length;
		int vertex_count = n + m + 2;
		FlowGraph graph = new FlowGraph(vertex_count);
		for(int i=0; i<n; i++) graph.addEdge(0, i+1, 1);
		for(int i=0; i<m; i++) graph.addEdge(n+1+i, n+m+1, 1);
		for(int i=0; i<n;i++){
			for(int j=0; j<m;j++){
				if(!bipartiteGraph[i][j]) continue;
				graph.addEdge(i+1, n+1+j, 1);
			}
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

	private int[] findMatching(boolean[][] bipartiteGraph) {
		// Replace this code with an algorithm that finds the maximum
		// matching correctly in all cases.
		int numLeft = bipartiteGraph.length;
		int numRight = bipartiteGraph[0].length;

		int[] matching = new int[numLeft];
		FlowGraph graph = readGraph(bipartiteGraph);
		maxFlow(graph);
		for(int i=0; i<numLeft;i++){
			for(int id: graph.getIds(i+1)){
				Edge edge = graph.getEdge(id);
				if(edge.flow==1){
					matching[i] = edge.to - numLeft;
					break;
				}
				matching[i] = -1;
			}
		}

		return matching;
	}

	private void writeResponse(int[] matching) {
		for (int i = 0; i < matching.length; ++i) {
			if (i > 0) {
				out.print(" ");
			}
			if (matching[i] == -1) {
				out.print("-1");
			} else {
				out.print(matching[i]);
			}
		}
		out.println();
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
