import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class GSMNetwork {
	 private final InputReader reader;
	 private final OutputWriter writer;

	 public GSMNetwork(InputReader reader, OutputWriter writer) {
		  this.reader = reader;
		  this.writer = writer;
	 }

	 public static void main(String[] args) {
		  InputReader reader = new InputReader(System.in);
		  OutputWriter writer = new OutputWriter(System.out);
		  new GSMNetwork(reader, writer).run();
		  writer.writer.flush();
	 }

	 class Edge {
		  int from;
		  int to;
	 }

	 class ConvertGSMNetworkProblemToSat {
		  int numVertices;
		  Edge[] edges;

		  ConvertGSMNetworkProblemToSat (int n, int m) {
				numVertices = n;
				edges = new Edge[m];
				for (int i = 0; i < m; ++i) {
					 edges[i] = new Edge();
				}
		  }

		  void printEquisatisfiableSatFormula() {
				writer.printf("%d %d\n", numVertices+edges.length*3,  numVertices*3);
				for(int i= 1; i<=numVertices; i++){
					writer.printf("%d %d %d 0\n", i, i+numVertices, i+numVertices*2);
				}
				for(Edge edge: edges){
					writer.printf("%d %d 0\n", -edge.from, -edge.to);
					writer.printf("%d %d 0\n", -edge.from-numVertices, -edge.to-numVertices);
					writer.printf("%d %d 0\n", -edge.from-numVertices*2, -edge.to-numVertices*2);
				}
		  }
	 }

	 public void run() {
		  int n = reader.nextInt();
		  int m = reader.nextInt();

		  ConvertGSMNetworkProblemToSat  converter = new ConvertGSMNetworkProblemToSat (n, m);
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
