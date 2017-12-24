import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class ConnectedComponents {
	private static void dfs(ArrayList<Integer>[] adj, int x, boolean[] visited){
		Iterator<Integer> it = adj[x].iterator();
		while(it.hasNext()){
			int nextX = it.next();
			if (!visited[nextX]) {
				visited[nextX] = true;
				dfs(adj, nextX, visited);
			}
		}
	}

	private static int numberOfComponents(ArrayList<Integer>[] adj) {
		int result = 0;
		int n = adj.length;
		boolean[] visited = new boolean[n];
		for(int i=0; i<n; i++) visited[i] = false;
		for(int i=0; i<n; i++){
			if(!visited[i]){
				result++;
				visited[i] = true;
				dfs(adj, i, visited);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		for (int i = 0; i < n; i++) {
			adj[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < m; i++) {
			int x, y;
			x = scanner.nextInt();
			y = scanner.nextInt();
			adj[x - 1].add(y - 1);
			adj[y - 1].add(x - 1);
		}
		System.out.println(numberOfComponents(adj));
	}
}

