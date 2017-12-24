import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class Acyclicity {

	private static boolean foundCycle(ArrayList<Integer>[] adj, int x, int[] visited){
		Iterator<Integer> it = adj[x].iterator();
		while(it.hasNext()){
			int nextX = it.next();
			if (visited[nextX] == -1) {
				visited[nextX] = 0;
				boolean found = foundCycle(adj, nextX, visited);
				visited[nextX] = 1;
				if(found) return true;
			}
			if(visited[nextX] == 0) return true;
		}
		return false;
	}

	private static int acyclic(ArrayList<Integer>[] adj, int n) {
		int[] visited = new int[n];
		for(int i=0; i<n; i++) visited[i] = -1;
		for(int i=0; i<n; i++){
			visited[i] = 0;
			boolean found = foundCycle(adj, i, visited);
			if(found) return 1;
			visited[i] = 1;
		}
		return 0;
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
		}
		System.out.println(acyclic(adj, n));
	}
}

