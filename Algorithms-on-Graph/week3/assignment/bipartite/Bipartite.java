import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {
	private static int bipartite(ArrayList<Integer>[] adj) {
		int[] partitions = new int[adj.length];
		for(int i=0; i<adj.length; i++) partitions[i] = 0;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(0);
		partitions[0] = 1;
		while(!q.isEmpty()){
			int x = q.poll();
			for(int y: adj[x]){
				if(partitions[y]==partitions[x]) return 0;
				if (partitions[y]==0) {
					partitions[y] = -partitions[x];
					q.add(y);
				}
			}
		}
		return 1;
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
		System.out.println(bipartite(adj));
	}
}

