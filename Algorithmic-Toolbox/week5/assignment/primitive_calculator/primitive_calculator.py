# Uses python3
import sys

def optimal_sequence(n):
	pathes = [None] * (n+1)
	pathes[1] = [1]
	for i in range(1,n):
		if pathes[i] is None: continue
		if i+1<=n and (pathes[i+1] is None or len(pathes[i+1])>len(pathes[i])+1): 
			pathes[i+1] = pathes[i]+[i+1]
		if 2*i<=n and (pathes[2*i] is None or len(pathes[2*i])>len(pathes[i])+1): 
			pathes[2*i] = pathes[i]+[2*i]
		if 3*i<=n and (pathes[3*i] is None or len(pathes[3*i])>len(pathes[i])+1): 
			pathes[3*i] = pathes[i]+[3*i]
	return pathes[n]

input = sys.stdin.read()
n = int(input)
sequence = list(optimal_sequence(n))
print(len(sequence) - 1)
for x in sequence:
	print(x, end=' ')
