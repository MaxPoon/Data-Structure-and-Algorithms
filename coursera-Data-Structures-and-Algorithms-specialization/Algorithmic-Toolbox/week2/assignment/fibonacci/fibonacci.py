# Uses python3
def calc_fib(n):
	if (n <= 1):
		return n
	Phi = (1+5**(0.5))/2
	phi = (1-5**(0.5))/2
	ans = (Phi**n - (phi)**n)/(5**0.5)
	return int(ans+0.5)

n = int(input())
print(calc_fib(n))
