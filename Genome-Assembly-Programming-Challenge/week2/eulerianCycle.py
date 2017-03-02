#Uses python3

data = input().split()
n = int(data[0])
m = int(data[1])
edges = []
for i in range(m):
	edges.append(list(map(int, input().split())))
adj = [[] for _ in range(n)]
number = 0
for (a, b) in edges:
	adj[a - 1].append(((b - 1), number))
	number += 1

degree = [0]*n
for s, e in edges:
	degree[s-1]+=1
	degree[e-1]-=1
if any(degree):
	print(0)
else:
	path = [0]
	visited = set()
	while len(visited)<len(edges):
		for i, point in enumerate(path):
			allVisited = True
			for nextPoint in adj[point]:
				if nextPoint[1] not in visited:
					allVisited = False
					break
			if allVisited: continue
			new_cycle = [point]
			current = point
			findNext = True
			while findNext:
				findNext = False
				for nextPoint in adj[current]:
					if nextPoint[1] not in visited:
						visited.add(nextPoint[1])
						new_cycle.append(nextPoint[0])
						current = nextPoint[0]
						findNext = True
						break
			break
		path = path[:i]+new_cycle+path[i+1:]
	path = list(map(lambda x: str(x+1), path))[:-1]
	print(1)
	print(" ".join(path))