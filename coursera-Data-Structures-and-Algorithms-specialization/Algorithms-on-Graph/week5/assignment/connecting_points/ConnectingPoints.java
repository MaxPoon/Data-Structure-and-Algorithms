import java.util.*;

public class ConnectingPoints {
	private static double minimumDistance(int[] x, int[] y) {
		double result = 0.;
		int n = x.length;
		int count = 0;
		int[] union = new int[n];
		for(int i=0; i<n; i++) union[i] = i;
		PriorityQueue<Edge> edges = new PriorityQueue<Edge>(n*(n-1)/2, (a,b) -> {
			if (a.length > b.length) return 1;
			if (a.length == b.length) return 0;
			return -1;
		});
		for(int i=0; i<n-1; i++){
			for(int j=i+1; j<n; j++){
				Edge e = new Edge(i, j, x[i], y[i], x[j], y[j]);
				edges.offer(e);
			}
		}
		while(count<n-1){
			Edge e = edges.poll();
			int startParent = e.start;
			int endParent = e.end;
			while(startParent!= union[startParent]) startParent=union[startParent];
			while(endParent!=union[endParent]) endParent = union[endParent];
			if(startParent==endParent) continue;
			union[e.start] = startParent;
			union[e.end] = startParent;
			union[endParent] = startParent;
			result += e.length;
			count++;
		}
		return result;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] x = new int[n];
		int[] y = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = scanner.nextInt();
			y[i] = scanner.nextInt();
		}
		System.out.println(minimumDistance(x, y));
	}
}

class Edge{
	public int start, end;
	public double length;
	Edge(int start, int end, int startX, int startY, int endX, int endY){
		this.start = start;
		this.end = end;
		this.length = Math.sqrt(Math.pow((startX-endX),2)+Math.pow((startY-endY),2));
	}
}