import java.util.*;

public class Inversions {

	private static long getNumberOfInversions(int[] a, int[] b, int left, int right) {
		long numberOfInversions = 0;
		if (right <= left) {
			return numberOfInversions;
		}
		int ave = (left + right) / 2;
		numberOfInversions += getNumberOfInversions(a, b, left, ave);
		numberOfInversions += getNumberOfInversions(a, b, ave+1, right);
		//write your code here
		numberOfInversions += merge(a, left, ave, right, b);
		return numberOfInversions;
	}

	private static long merge(int[] a, int start, int mid, int end, int[] b) {
		int left = start;
		int right = mid+1;
		int index = start;
		long numberOfInversions = 0;
		
		// merge two sorted subarrays in a to b array
		while (left <= mid && right <= end) {
			if (a[left] <= a[right]) {
				b[index++] = a[left++];
			} else {
				numberOfInversions += (mid - left + 1);
				b[index++] = a[right++];
			}
		}
		// if (left <= mid) numberOfInversions += (mid - left +1)*(end - mid);
		while (left <= mid) {
			b[index++] = a[left++];
		}
		while (right <= end) {
			b[index++] = a[right++];
		}
		
		// copy b back to a
		for (index = start; index <= end; index++) {
			a[index] = b[index];
		}
		return numberOfInversions;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = scanner.nextInt();
		}
		int[] b = new int[n];
		System.out.println(getNumberOfInversions(a, b, 0, a.length-1));
	}
}

