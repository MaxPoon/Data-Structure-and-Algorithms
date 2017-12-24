import java.util.Scanner;

public class PlacingParentheses {
	private static long getMaximValue(String exp) {
		//write your code here
		int n = 1;
		for(int i=0; i<exp.length();i++){
			if(exp.charAt(i)=='+' || exp.charAt(i)=='-' || exp.charAt(i)=='*') n++;
		}
		long[] numbers = new long[n];
		String[] numbers_str = new String[n];
		char[] ops = new char[n-1];
		int p=0;
		numbers_str = exp.split("[\\D]");
		for(int i=0;i<n;i++) numbers[i] = Integer.parseInt(numbers_str[i]);
		for(int i=0; i<exp.length();i++){
			if(exp.charAt(i)=='+' || exp.charAt(i)=='-' || exp.charAt(i)=='*') ops[p++]=exp.charAt(i);
		}
		if(n==2) return eval(numbers[0],numbers[1],ops[0]);
		long[][] M = new long[n][n];
		long[][] m = new long[n][n];
		long M1,M2,M3,M4,m1,m2,m3,m4, M_temp, m_temp, temp;
		for(int i=0; i<n; i++) {
			M[i][i] = numbers[i];
			m[i][i] = numbers[i];
		}
		for(int l=1; l<n; l++){
			for(int i=0; i<n-l; i++){
				int j = i+l;
				M_temp = eval(M[i][i], M[i+1][j], ops[i]);
				m_temp = M_temp;
				for (int k=i; k<j; k++){
					temp = eval(M[i][k], M[k+1][j], ops[k]);
					M_temp = Math.max(M_temp, temp);
					m_temp = Math.min(m_temp, temp);
					temp = eval(M[i][k], m[k+1][j], ops[k]);
					M_temp = Math.max(M_temp, temp);
					m_temp = Math.min(m_temp, temp);
					temp = eval(m[i][k], M[k+1][j], ops[k]);
					M_temp = Math.max(M_temp, temp);
					m_temp = Math.min(m_temp, temp);
					temp = eval(m[i][k], m[k+1][j], ops[k]);
					M_temp = Math.max(M_temp, temp);
					m_temp = Math.min(m_temp, temp);
				}
				M[i][j] = M_temp;
				m[i][j] = m_temp;
			}
		}
		return M[0][n-1];
	}

	private static long eval(long a, long b, char op) {
		if (op == '+') {
			return a + b;
		} else if (op == '-') {
			return a - b;
		} else if (op == '*') {
			return a * b;
		} else {
			assert false;
			return 0;
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String exp = scanner.next();
		System.out.println(getMaximValue(exp));
	}
}

