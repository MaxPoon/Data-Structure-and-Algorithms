import java.io.*;
import java.util.*;

public class CleaningApartment {
	private final InputReader reader;
	private final OutputWriter writer;

	public CleaningApartment(InputReader reader, OutputWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	public static void main(String[] args) {
		InputReader reader = new InputReader(System.in);
		OutputWriter writer = new OutputWriter(System.out);
		new CleaningApartment(reader, writer).run();
		writer.writer.flush();
	}

	class Edge {
		int from;
		int to;
	}

	class ConvertHampathToSat {
		int numVertices;
		Edge[] edges;

		ConvertHampathToSat(int n, int m) {
			numVertices = n;
			edges = new Edge[m];
			for (int i = 0; i < m; ++i) {
				edges[i] = new Edge();
			}
		}

		void printEquisatisfiableSatFormula() {
			LinkedList<LinkedList> clauses = new LinkedList<LinkedList>();
			boolean[][] adj = new boolean[numVertices][numVertices];
			formAdjMatrix(adj);
			addExactlyOnce(clauses);
			addConstraints(clauses, adj);
			oneVertexEachPosition(clauses);
			writer.printf("%d %d\n", clauses.size(), numVertices*numVertices);
			for(LinkedList<Integer> clause: clauses){
				for(int v: clause) writer.printf("%d ",v);
				writer.printf("0\n");
			}
		}

		//each vertex appear in the path exactly once
		void addExactlyOnce(LinkedList<LinkedList> clauses){
			for(int i=1; i<=numVertices; i++){
				LinkedList<Integer> clause = new LinkedList<Integer>();
				for(int j=0; j<numVertices; j++) clause.add(i+j*numVertices);
				clauses.add(clause);
				for(int j=0; j<numVertices-1; j++){
					for(int k=j+1; k<numVertices; k++){
						clause = new LinkedList<Integer>();
						clause.add(-i-j*numVertices);
						clause.add(-i-k*numVertices);
						clauses.add(clause);
					}
				}
			}
		}

		void oneVertexEachPosition(LinkedList<LinkedList> clauses){
			for(int k=0;k<numVertices;k++){
				LinkedList<Integer> clause = new LinkedList<Integer>();
				for(int i=1; i<=numVertices; i++) clause.add(i+k*numVertices);
				clauses.add(clause);
				for(int i=1;i<=numVertices-1;i++){
					for(int j=i+1;j<=numVertices; j++){
						clause = new LinkedList<Integer>();
						clause.add(-i-k*numVertices);
						clause.add(-j-k*numVertices);
						clauses.add(clause);
					}
				}
			}
		}
		void formAdjMatrix(boolean[][] adj){
			for(Edge edge: edges){
				adj[edge.from-1][edge.to-1] = true;
				adj[edge.to-1][edge.from-1] = true;
			}
		}

		void addConstraints(LinkedList<LinkedList> clauses, boolean[][] adj){
			for(int i=1; i<=numVertices-1; i++){
				for(int j=i; j<=numVertices; j++){
					if(!adj[i-1][j-1] && i!=j){
						for(int k=0; k<numVertices-1; k++){
							LinkedList<Integer> clause = new LinkedList<Integer>();
							clause.add(-i-k*numVertices);
							clause.add(-j-(k+1)*numVertices);
							clauses.add(clause);
							clause = new LinkedList<Integer>();
							clause.add(-j-k*numVertices);
							clause.add(-i-(k+1)*numVertices);
							clauses.add(clause);
						}
					}
				}
			}
		}
	}

	public void run() {
		int n = reader.nextInt();
		int m = reader.nextInt();

		ConvertHampathToSat converter = new ConvertHampathToSat(n, m);
		for (int i = 0; i < m; ++i) {
			converter.edges[i].from = reader.nextInt();
			converter.edges[i].to = reader.nextInt();
		}

		converter.printEquisatisfiableSatFormula();
	}

	static class InputReader {
		public BufferedReader reader;
		public StringTokenizer tokenizer;

		public InputReader(InputStream stream) {
			reader = new BufferedReader(new InputStreamReader(stream), 32768);
			tokenizer = null;
		}

		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

	static class OutputWriter {
		public PrintWriter writer;

		OutputWriter(OutputStream stream) {
			writer = new PrintWriter(stream);
		}

		public void printf(String format, Object... args) {
			writer.print(String.format(Locale.ENGLISH, format, args));
		}
	}
}
