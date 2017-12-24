import java.io.*;
import java.util.*;

public class Diet {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	void solve() throws IOException {
		int n = nextInt();
		int m = nextInt();
		double[][] A = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				A[i][j] = nextInt();
			}
		}
		double[] b = new double[n];
		for (int i = 0; i < n; i++) {
			b[i] = nextInt();
		}
		double[] c = new double[m];
		for (int i = 0; i < m; i++) {
			c[i] = nextInt();
		}
		LinearEquationsSolver ls = new LinearEquationsSolver(A, b, c);
		ls.print();
	}

	Diet() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		solve();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		new Diet();
	}

	String nextToken() {
		while (st == null || !st.hasMoreTokens()) {
			try {
				st = new StringTokenizer(br.readLine());
			} catch (Exception e) {
				eof = true;
				return null;
			}
		}
		return st.nextToken();
	}

	int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}
}

class LinearEquationsSolver {
	double[][] A; //co-efficient matrix
	double[] b; //output matrix for co-efficicnet
	double[] c; //objective function matrix

	int n;//no of input equations
	int m; // no of variables
	double maxValue = Double.NEGATIVE_INFINITY;
	double[] result = null;
	static double INFINITY = Math.pow(10,9);
	private boolean bounded = true;

	LinearEquationsSolver(double[][] A, double[] b, double[] c){
		n = A.length;
		m = c.length;
		this.A = A;
		this.b = b;
		this.c = c;
		//total no of inequalities = n +m + 1(for infinity)
		int total = n + m +1;
		compute(total);
	}

	public void print(){
		if (maxValue == Double.NEGATIVE_INFINITY){
			System.out.println("No solution");
			return;
		}
		if (!bounded){
			System.out.println("Infinity");
			return;
		}
		System.out.println("Bounded solution");
		for (int i=0; i< result.length; i++){
			System.out.print(String.format("%.15f", result[i]) + " ");
		}
	}

	private void compute(int total){
		int[] arr = new int[total];
		for (int i=0; i < arr.length; i++){
			arr[i] = i;
		}
		genProcessCombinations(arr,total, m);
	}

	/**
	 * Process subset of size
	 * @param subset
	 */
	private void processSubset(Set<Integer> subset){
		double[][] A = new double[m][m];
		double[] b = new double[m];
		updateMatrices(subset, A, b);
		GaussianElimination gElim = new GaussianElimination(A, b);
		if (!gElim.hasSolution){
			return;
		}
		double[] temp_result = gElim.b;
		if (!satisfiesAllInEq(temp_result)){
			return;
		}
		double val = computeVal(temp_result);
		if (val > maxValue){
			maxValue = val;
			result = temp_result;
			if (subset.contains(n+m)){
				bounded = false;
			} else{
				bounded = true;
			}
		}
	}

	/**
	 * Verify result satisfies all the equations
	 * @param result
	 * @return
	 */
	private boolean satisfiesAllInEq(double[] result){
		boolean satisfied = true;
		//check to see if eq satisfies regular equations
		for (int i=0; i < n; i++){
			double inEqSum = 0;
			for(int j= 0; j < m; j++){
					inEqSum += result[j] * this.A[i][j];
			}
			if (inEqSum > b[i] + Math.pow(10, -3)){
				//not satisfied
				satisfied = false;
				break;
			}
		}
		//check to see if it satisfies constraints
		for (int i=0 ; i < m; i++){
			if (result[i] * -1 > Math.pow(10, -3)){
				//not satisfied
				satisfied = false;
				break;
			}

		}
		return  satisfied;

	}

	private double computeVal(double[] result){
		double val = 0;
		for (int i=0; i < result.length; i++){
			val += result[i] * c[i];
		}
		return val;
	}

	/**
	 * Update matrices
	 */
	private void updateMatrices(Set<Integer> set, double[][] A, double[] b){
		int index = 0;
		for (Integer val: set) {
			if (val < n) {
				A[index] = this.A[val];
				b[index] = this.b[val];
			}
			else if (val < n+m){
				int diff = val - n;
				A[index] = new double[m];
				A[index][diff] = -1;
				b[index] = 0;
			}else{
				A[index] = new double[m];
				Arrays.fill(A[index], 1);
				b[index] = INFINITY;
			}
			index += 1;
		}
	}


	/**
	  arr[]  ---> Input Array
	data[] ---> Temporary array to store current combination
	start & end ---> Staring and Ending indexes in arr[]
	index  ---> Current index in data[]
	r ---> Size of a combination to be printed
	 **/
	void combinationUtil(int arr[], int n, int r, int index,
								int data[], int i)
	{
		// Current combination is ready to be printed, print it
		if (index == r)
		{
			Set<Integer> set = new HashSet<>();
			for (int j=0; j<r; j++)
				set.add(data[j]);
			processSubset(set);
			return;
		}

		// When no more elements are there to put in data[]
		if (i >= n)
			return;

		// current is included, put next at next location
		data[index] = arr[i];
		combinationUtil(arr, n, r, index+1, data, i+1);

		// current is excluded, replace it with next (Note that
		// i+1 is passed, but index is not changed)
		combinationUtil(arr, n, r, index, data, i+1);
	}

	/**
	 *     The main function that prints all combinations of size r
	 *      in arr[] of size n. This function mainly uses combinationUtil()
	 */
	 void genProcessCombinations(int arr[], int n, int r)
	{
		// A temporary array to store all combination one by one
		int data[]=new int[r];
		//Set<Set<Integer>> result = new HashSet<Set<Integer>>();
		// Print all combination using temprary array 'data[]'
		combinationUtil(arr, n, r, 0, data, 0);
	}
}

class GaussianElimination {
	private double[][] A; //refers to co-efficient matrix
	double[] b; //refers to output matrix
	boolean hasSolution = true;


	GaussianElimination(double[][] A, double[] b){
		if (A == null || b == null){
			hasSolution = false;
			return;
		}
		if (A.length == 0 || b.length == 0){
			hasSolution = false;
			return;
		}
		copyMatrix(A);
		copyMatrix(b);
		rowReduce();
	}

	private void rowReduce(){
		int rowLength = A.length;
		for (int row =0; row < rowLength; row++){
			int rowPivot = getRowPivot(A, row);
			if (rowPivot == -1){
				hasSolution = false;
				return;
			}
			if (rowPivot != row){
				//swap rows
				swapRowsInA(row, rowPivot);
				swapIndexInb(row, rowPivot);
			}
			//pivot element is located in col <row> for current row < row>
			//rescale to make pivot as 1
			if (A[row][row] != 1){
				//rescale entire row
				rescalePivot(row);
			}
			//make col zero
			for (int r = 0; r < rowLength; r++){
				if (row == r){
					continue;
				}
				makeColZero(row, r);

			}
		}
	}

	/**
	 * Print result of b
	 */
	public void printResult(){
		if (hasSolution) {
			for (int row = 0; row < b.length; row++) {
				System.out.print(b[row] + " ");
			}
		}
	}

	/**
	 * Make col entries as zero for the pivot row of A and update b
	 * @param current_row pi
	 * @param row
	 */
	private void makeColZero(int current_row, int row){
		double scale_factor = A[row][current_row];
		for (int col = 0; col < A[0].length; col++){
			A[row][col] = A[row][col] - scale_factor * A[current_row][col];
		}
		b[row] = b[row] - scale_factor * b[current_row];
	}

	private void rescalePivot(int row){
		double scale_factor = A[row][row];
		for (int col =0 ; col < A[0].length; col++){
			A[row][col] = A[row][col]/scale_factor;
		}
		b[row] = b[row] / scale_factor;
	}

	/**
	 * Swap rows i and j of A
	 */
	private void swapRowsInA(int i, int j){
		double[] temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}

	/**
	 * Swap values in ith and jth index of b
	 */
	private void swapIndexInb(int i, int j){
		double temp = b[i];
		b[i] = b[j];
		b[j] = temp;
	}

	private int getRowPivot(double[][] matrix, int row){
		//select first non zero entry in left most column
		for (int r = row; r < matrix.length; r++){
			if (matrix[r][row] != 0){
				return r;
			}
		}
		return -1;
	}

	/**
	 * Copy input A to have a local copy < avoid modifying client</>
	 * @param matrix
	 */
	private void copyMatrix(double[][] matrix){
		A = new double[matrix.length][matrix[0].length];
		for (int i=0; i < matrix.length; i++){
			for (int j=0; j< matrix[0].length; j++){
				this.A[i][j] = matrix[i][j];
			}
		}
	}

	/**
	 * Copy input B to have a local copy < avoid modifying client</>
	 * @param matrix
	 */
	private void copyMatrix(double[] matrix){
		b = new double[matrix.length];
		for (int i=0; i< matrix.length; i++){
				b[i] = matrix[i];
		}

	}

}