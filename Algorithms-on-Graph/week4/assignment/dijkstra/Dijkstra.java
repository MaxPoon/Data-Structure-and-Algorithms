import java.util.*;

public class Dijkstra {
	private static int distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
		PriorityQueue<Node> q = new PriorityQueue<Node>(adj.length, (a,b) -> a.distance - b.distance);
		Node start = new Node(s, 0);
		q.offer(start);
		int[] dist = new int[adj.length];
		for(int i=0; i<adj.length; i++) dist[i] = -1;
		dist[s] = 0;
		while(!q.isEmpty()){
			Node x = q.poll();
			if(x.number == t) return x.distance;
			Iterator<Integer> costIt = cost[x.number].iterator();
			for(int y: adj[x.number]){
				int d = costIt.next();
				if(dist[y]==-1 || dist[y]>dist[x.number]+d){
					dist[y] = dist[x.number] + d;
					Node newNode = new Node(y, dist[y]);
					q.offer(newNode);
				}
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
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
		int x = scanner.nextInt() - 1;
		int y = scanner.nextInt() - 1;
		System.out.println(distance(adj, cost, x, y));
	}
}

class Node{
	public int distance, number;
	Node(int number, int distance){
		this.number = number;
		this.distance = distance;
	}
}