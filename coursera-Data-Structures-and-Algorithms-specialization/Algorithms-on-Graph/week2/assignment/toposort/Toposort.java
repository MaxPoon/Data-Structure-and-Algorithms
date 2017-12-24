import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Iterator;

public class Toposort {
	private static ArrayList<Integer> toposort(boolean[] isSink, ArrayList<Integer>[] adjR, int n) {
		boolean used[] = new boolean[n];
		for(int i=0; i<n; i++) used[i] = false;
		ArrayList<Integer> order = new ArrayList<Integer>();
		for(int i=0; i<n; i++){
			if(isSink[i]) {
				dfs(adjR, used, order, i);
				order.add(i);
			}
		}
		return order;
	}

	private static void dfs(ArrayList<Integer>[] adjR, boolean[] used, ArrayList<Integer> order, int x) {
		Iterator<Integer> it = adjR[x].iterator();
		while(it.hasNext()){
			int nextX = it.next();
			if (!used[nextX]) {
				used[nextX] = true;
				dfs(adjR, used, order, nextX);
				order.add(nextX);
			}
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		boolean[] isSink = new boolean[n];
		ArrayList<Integer>[] adjR = (ArrayList<Integer>[])new ArrayList[n];
		for (int i = 0; i < n; i++) {
			isSink[i] = true;
			adjR[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < m; i++) {
			int x, y;
			x = scanner.nextInt();
			y = scanner.nextInt();
			isSink[x-1] = false;
			adjR[y - 1].add(x - 1);
		}
		ArrayList<Integer> order = toposort(isSink, adjR, n);
		for (int x : order) {
			System.out.print((x + 1) + " ");
		}
	}
}

