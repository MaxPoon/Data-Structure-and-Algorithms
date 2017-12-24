import java.util.*;

public class LCM {
	private static int gcd(int a, int b) {
		boolean a_odd, b_odd;
		if (a != b){
			a_odd = ((a & 1) == 1);
			b_odd = ((b & 1) == 1);
			if (!a_odd && !b_odd) return (gcd(a>>1, b>>1))<<1;
			else if (!a_odd && b_odd) return (gcd(a>>1, b));
			else if (a_odd && !b_odd) return (gcd(a, b>>1));
			else{
				if(a > b) return gcd(b, (a-b)>>1);
				else return gcd(a, (b-a)>>1);
			}
		}
		else return a;
	}

	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int a = scanner.nextInt();
		int b = scanner.nextInt();

		System.out.println((long)a*b/gcd(a, b));
	}
}
