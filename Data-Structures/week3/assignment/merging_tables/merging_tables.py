# python3

import sys

n, m = map(int, sys.stdin.readline().split())
lines = list(map(int, sys.stdin.readline().split()))
rank = [1] * n
parent = list(range(0, n))
ans = max(lines)

def getParent(table):
	# find parent and compress path
	return parent[table]

def merge(destination, source):
	global ans
	realDestination, realSource = getParent(destination), getParent(source)

	if realDestination == realSource:
		return False

	# merge two components
	if rank[realDestination] < rank[realSource]:
		parent[realDestination] = realSource
		lines[realSource] += lines[realDestination]
		ans = max(ans, lines[realSource])
	else:
		parent[realSource] = realDestination
		lines[realDestination] += lines[realSource]
		ans = max(ans, lines[realDestination])
		if rank[realDestination] == rank[realSource]:
			rank[realDestination] += 1
	# use union by rank heuristic 
	# update ans with the new maximum table size
	
	return True

for i in range(m):
	destination, source = map(int, sys.stdin.readline().split())
	merge(destination - 1, source - 1)
	print(ans)
	
