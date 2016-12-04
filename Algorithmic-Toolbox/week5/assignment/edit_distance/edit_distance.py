# Uses python3
def edit_distance(s, t):
	#write your code here
	m , n = len(s) , len(t)
	dp=[[0 for i in range(n+1)] for j in range(m+1)]
	for i in range(m+1): dp[i][0]=i
	for j in range(n+1): dp[0][j]=j;
	
	for i in range(1,m+1):
		for j in range(1,n+1):
			dp[i][j]=min(dp[i-1][j-1] + (0 if s[i-1]==t[j-1] else 1) , \
						min(dp[i-1][j]+1,dp[i][j-1]+1) )
	return dp[m][n]

if __name__ == "__main__":
	print(edit_distance(input(), input()))
