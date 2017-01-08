import java.io.*;
import java.util.*;

public class CircuitDesign {
	private final InputReader reader;
	private final OutputWriter writer;

	public CircuitDesign(InputReader reader, OutputWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	public static void main(String[] args) {
      new Thread(null, new Runnable() {
			public void run() {
				InputReader reader = new InputReader(System.in);
				OutputWriter writer = new OutputWriter(System.out);
				new CircuitDesign(reader, writer).run();
				writer.writer.flush();
			}
		}, "1", 1 << 26).start();
   }

	class Clause {
		int firstVar;
		int secondVar;
	}

	class TwoSatisfiability {
		int numVars;
		Clause[] clauses;
		ArrayList<HashSet<Integer>> sccs = new ArrayList<HashSet<Integer>>();

		TwoSatisfiability(int n, int m) {
			numVars = n;
			clauses = new Clause[m];
			for (int i = 0; i < m; ++i) {
				clauses[i] = new Clause();
			}

		}

		void setup(){
			ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[2*numVars];
			ArrayList<Integer>[] adjR = (ArrayList<Integer>[])new ArrayList[2*numVars];
			for (int i = 0; i < 2*numVars; i++) {
				adj[i] = new ArrayList<Integer>();
				adjR[i] = new ArrayList<Integer>();
			}
			constructImplicationGraph(adj, adjR);
			LinkedList<Integer> order = new LinkedList<Integer>();
			boolean[] visited = new boolean[2*numVars];
			for(int i=0; i<2*numVars; i++){
				if(!visited[i]){
					visited[i] = true;
					dfs1(adjR, visited, i, order);
					order.add(i);
				}
			}
			Collections.reverse(order);
			for(int i=0; i<adj.length; i++) visited[i] = false;
			
			for(int x: order){
				if(!visited[x]){
					HashSet<Integer> scc = new HashSet<Integer>();
					visited[x] = true;
					dfs2(adj, visited, x, scc);
					sccs.add(scc);
				}
			}
		}

		boolean isSatisfiable(int[] result) {
			for(HashSet<Integer> scc: sccs){
				for(int x: scc){
					if(scc.contains(x+numVars) || scc.contains(x-numVars)) return false;
				}
			}
			for(int i=0; i<result.length; i++) result[i] = -1;
			for(HashSet<Integer> scc: sccs){
				for(int i: scc){
					int x;
					boolean isNegative;
					if(i>=numVars){
						x = i-numVars;
						isNegative = true;
					}else{
						x = i;
						isNegative = false;
					}
					if(result[x]==-1){
						if(isNegative)	result[x] = 1;
						else result[x] = 0;
					}
				}
			}
			return true;
		}

		void constructImplicationGraph(ArrayList<Integer>[] adj, ArrayList<Integer>[] adjR){

			for(Clause clause: clauses){
				int x, y, notX, notY;
				
				if(clause.firstVar>0) {
					x = clause.firstVar-1;
					notX = x + numVars;
				}else{
					x = -clause.firstVar - 1 + numVars;
					notX = x - numVars;
				}

				if(clause.secondVar>0) {
					y = clause.secondVar-1;
					notY = y + numVars;
				}else {
					y = -clause.secondVar - 1 + numVars;
					notY = y - numVars;
				}

				adj[notX].add(y);
				adj[notY].add(x);
				adjR[y].add(notX);
				adjR[x].add(notY);
			}
		}

		void dfs1(ArrayList<Integer>[] adj, boolean[] visited, int x, LinkedList<Integer> order){
			for(int nextX: adj[x]){
				if(!visited[nextX]){
					visited[nextX] = true;
					dfs1(adj, visited, nextX, order);
					order.add(nextX);
				}
			}
		}

		void dfs2(ArrayList<Integer>[] adj, boolean[] visited, int x, HashSet<Integer> scc){
			scc.add(x);
			for(int nextX: adj[x]){
				if(!visited[nextX]){
					visited[nextX] = true;
					dfs2(adj, visited, nextX, scc);
				}
			}
		}
	}

	public void run() {
		int n = reader.nextInt();
		int m = reader.nextInt();

		TwoSatisfiability twoSat = new TwoSatisfiability(n, m);
		for (int i = 0; i < m; ++i) {
			twoSat.clauses[i].firstVar = reader.nextInt();
			twoSat.clauses[i].secondVar = reader.nextInt();
		}
		twoSat.setup();

		int result[] = new int[n];
		if (twoSat.isSatisfiable(result)) {
			writer.printf("SATISFIABLE\n");
			for (int i = 1; i <= n; ++i) {
				if (result[i-1] == 1) {
					writer.printf("%d", -i);
				} else {
					writer.printf("%d", i);
				}
				if (i < n) {
					writer.printf(" ");
				} else {
					writer.printf("\n");
				}
			}
		} else {
			writer.printf("UNSATISFIABLE\n");
		}
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
