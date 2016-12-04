import java.util.*;

public class Knapsack {
	static int optimalWeight(int W, int[] w) {
		//write you code here
		int result = 0;
		int[][] dp = new int[w.length+1][W+1];
		for(int i=0; i<W+1; i++) dp[0][i] = 0;
		for(int i=0; i<w.length+1; i++) dp[i][0] = 0;
		for(int i=1; i<w.length+1; i++){
			for(int j=1; j<W+1; j++){
				if (w[i-1]> j) dp[i][j] = dp[i-1][j];
				else{
					dp[i][j] = Math.max(dp[i-1][j],dp[i-1][j-w[i-1]]+w[i-1]);
				}
			}
		}
		return dp[w.length][W];
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int W, n;
		W = scanner.nextInt();
		n = scanner.nextInt();
		int[] w = new int[n];
		for (int i = 0; i < n; i++) {
			w[i] = scanner.nextInt();
		}
		System.out.println(optimalWeight(W, w));
	}
}

