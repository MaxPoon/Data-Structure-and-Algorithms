import java.util.*;

public class ShortestPaths {

	private static void shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, long[] distance, int[] reachable, HashSet<Integer> arbitrage) {
		int n = adj.length;
		distance[s] = 0;
		reachable[s] = 1;
		for(int i=0; i<n-1; i++){
			boolean changed = false;
			for(int x=0;x<n;x++){
				if(distance[x]==Long.MAX_VALUE) continue;
				Iterator<Integer> costIt = cost[x].iterator();
				for(int y: adj[x]){
					int d = costIt.next();
					if(distance[y]>distance[x]+d){
						distance[y] = distance[x] + d;
						changed = true;
					}
				}
			}
			if(!changed) for(int j=0; j<n; j++) if(distance[j]<Long.MAX_VALUE) reachable[j]=1;
		}
		for(int i=0; i<n; i++) if(distance[i]<Long.MAX_VALUE) reachable[i]=1;
		for(int x=0;x<n;x++){
			if(distance[x]==Long.MAX_VALUE) continue;
			Iterator<Integer> costIt = cost[x].iterator();
			for(int y: adj[x]){
				int d = costIt.next();
				if(distance[y]>distance[x]+d){
					distance[y] = distance[x] + d;
					if(reachable[y]==2) continue;
					Queue<Integer> q = new LinkedList<Integer>();
					q.add(y);
					reachable[y] = 2;
					while(!q.isEmpty()){
						int v = q.poll();
						for(int u: adj[v]){
							if (reachable[u]!=2) {
								reachable[u] = 2;
								q.add(u);
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
		HashSet<Integer> arbitrage = new HashSet<Integer>();
		for (int i = 0; i < n; i++) {
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
		int s = scanner.nextInt() - 1;
		long distance[] = new long[n];
		int reachable[] = new int[n];
		for (int i = 0; i < n; i++) {
			distance[i] = Long.MAX_VALUE;
			reachable[i] = 0;
		}
		shortestPaths(adj, cost, s, distance, reachable, arbitrage);
		for (int i = 0; i < n; i++) {
			if (reachable[i] == 0) {
				System.out.println('*');
			} else if (reachable[i]==2) {
				System.out.println('-');
			} else {
				System.out.println(distance[i]);
			}
		}
	}

}

