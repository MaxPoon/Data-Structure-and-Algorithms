import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class Reachability {
	private static int reach(ArrayList<Integer>[] adj, int x, int y, boolean[] visited) {
		if (x == y) return 1;
		Iterator<Integer> it = adj[x].iterator();
		while(it.hasNext()){
			int nextX = it.next();
			if (!visited[nextX]) {
				visited[nextX] = true;
				int reached = reach(adj, nextX, y, visited);
				if (reached==1) return 1;
			}
		}
		return 0;
	}


	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		boolean[] visited = new boolean[n];
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		for (int i = 0; i < n; i++) {
			adj[i] = new ArrayList<Integer>();
			visited[i] = false;
		}
		for (int i = 0; i < m; i++) {
			int x, y;
			x = scanner.nextInt();
			y = scanner.nextInt();
			adj[x - 1].add(y - 1);
			adj[y - 1].add(x - 1);
		}
		int x = scanner.nextInt() - 1;
		int y = scanner.nextInt() - 1;
		visited[x] = true;
		System.out.println(reach(adj, x, y, visited));
	}
}

