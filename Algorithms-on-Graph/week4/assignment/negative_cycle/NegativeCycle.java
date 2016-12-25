import java.util.*;

public class NegativeCycle {
	private static int negativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
		int n = adj.length-1;
		int[] dist = new int[n+1];
		for(int i=0; i<n; i++) dist[i] = 99999;
		dist[n] = 0;
		for(int i=0; i<n; i++){
			boolean changed = false;
			for(int x=0;x<n+1;x++){
				Iterator<Integer> costIt = cost[x].iterator();
				for(int y: adj[x]){
					int d = costIt.next();
					if(dist[y]>dist[x]+d){
						changed = true;
						dist[y] = dist[x] + d;
					}
				}
			}
			if(!changed) return 0;
			if(changed && i==n-1) return 1;
		}

		return 0;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n+1];
		ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n+1];
		for (int i = 0; i < n+1; i++) {
			adj[i] = new ArrayList<Integer>();
			cost[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < m; i++) {
			int x, y, w;
			x = scanner.nextInt();
			y = scanner.nextInt();
			w = scanner.nextInt();
			adj[x - 1].add(y - 1);
			cost[x - 1].add(w);
		}
		for (int i=0; i<n; i++){
			adj[n].add(i);
			cost[n].add(0);
		}
		System.out.println(negativeCycle(adj, cost));
	}
}