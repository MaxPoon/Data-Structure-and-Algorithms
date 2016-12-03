import java.util.Scanner;
import java.util.*;

public class FractionalKnapsack {
	private static double getOptimalValue(int capacity, int[] values, int[] weights) {
		double value = 0;
		//write your code here
		ArrayList<Item> items = new ArrayList<Item>();
		for(int i=0; i<values.length; i++){
			Item item = new Item(values[i], weights[i]);
			items.add(item);
		}
		Collections.sort(items, Collections.reverseOrder());
		Iterator<Item> it = items.iterator();
		while(capacity > 0 && it.hasNext()){
			Item item = it.next();
			if (capacity >= item.weight){
				capacity -= item.weight;
				value += item.value;
			}else{
				value += capacity*item.unitPrice;
				break;
			}
		}
		return value;
	}

	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int capacity = scanner.nextInt();
		int[] values = new int[n];
		int[] weights = new int[n];
		for (int i = 0; i < n; i++) {
			values[i] = scanner.nextInt();
			weights[i] = scanner.nextInt();
		}
		System.out.println(getOptimalValue(capacity, values, weights));
	}
} 

class Item implements Comparable<Item>{
	public int weight, value;
	public double unitPrice;
	Item(int value, int weight){
		this.value = value;
		this.weight = weight;
		unitPrice = (double)value/weight;
	}

	public int compareTo(Item other){
		if (this.unitPrice > other.unitPrice) return 1;
		if (this.unitPrice < other.unitPrice) return -1;
		return 0;
		// return (int)(this.unitPrice - other.unitPrice);
	}
}