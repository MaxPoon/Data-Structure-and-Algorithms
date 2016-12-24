import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class StronglyConnected {
	private static void dfs1(ArrayList<Integer>[] adj, boolean[] visited, int x, ArrayList<Integer> order){
		for(int nextX: adj[x]){
			if(!visited[nextX]){
				visited[nextX] = true;
				dfs1(adj, visited, nextX, order);
				order.add(nextX);
			}
		}
	}

	private static void dfs2(ArrayList<Integer>[] adj, boolean[] visited, int x){
		for(int nextX: adj[x]){
			if(!visited[nextX]){
				visited[nextX] = true;
				dfs2(adj, visited, nextX);
			}
		}
	}

	private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj,ArrayList<Integer>[] adjR) {
		ArrayList<Integer> order = new ArrayList<Integer>();
		boolean[] visited = new boolean[adj.length];
		for(int i=0; i<adj.length; i++) visited[i] = false;
		for(int i=0; i<adj.length; i++){
			if(!visited[i]){
				visited[i] = true;
				dfs1(adjR, visited, i, order);
				order.add(i);
			}
		}
		Collections.reverse(order);
		int count = 0;
		for(int i=0; i<adj.length; i++) visited[i] = false;
		for(int x: order){
			if(!visited[x]){
				count++;
				visited[x] = true;
				dfs2(adj, visited, x);
			}
		}
		return count;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		ArrayList<Integer>[] adjR = (ArrayList<Integer>[])new ArrayList[n];
		for (int i = 0; i < n; i++) {
			adj[i] = new ArrayList<Integer>();
			adjR[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < m; i++) {
			int x, y;
			x = scanner.nextInt();
			y = scanner.nextInt();
			adj[x - 1].add(y - 1);
			adjR[y-1].add(x-1);
		}
		System.out.println(numberOfStronglyConnectedComponents(adj, adjR));
	}
}

