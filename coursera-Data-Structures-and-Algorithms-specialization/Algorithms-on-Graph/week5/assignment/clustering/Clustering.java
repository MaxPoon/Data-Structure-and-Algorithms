import java.util.*;

public class Clustering {
	private static double clustering(int[] x, int[] y, int k) {
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
		while(count<n-k){
			Edge e = edges.poll();
			int startParent = e.start;
			int endParent = e.end;
			while(startParent!= union[startParent]) startParent=union[startParent];
			while(endParent!=union[endParent]) endParent = union[endParent];
			if(startParent==endParent) continue;
			union[e.start] = startParent;
			union[e.end] = startParent;
			union[endParent] = startParent;
			count++;
		}
		double shortest = 999999.0;
		while(!edges.isEmpty()){
			Edge e = edges.poll();
			int startParent = e.start;
			int endParent = e.end;
			while(startParent!= union[startParent]) startParent=union[startParent];
			while(endParent!=union[endParent]) endParent = union[endParent];
			if(startParent==endParent) continue;
			double temp = e.length;
			if(temp<shortest) shortest=temp;
		}		
		return shortest;
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
		int k = scanner.nextInt();
		System.out.println(clustering(x, y, k));
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