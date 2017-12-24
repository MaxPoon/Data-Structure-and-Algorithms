# Uses python3
from collections import defaultdict   

DEFAULT_READS_NUMBER = 5396

reads = []
for _ in range(DEFAULT_READS_NUMBER):
    reads.append(input())
adj = defaultdict(list)
id_number = 0
for read in reads:
    adj[read[:-1]].append((read[1:],id_number))
    id_number += 1

path = [reads[0][:-1]]
visited = set()
while len(visited)<len(reads):
    for i, node in enumerate(path):
        allVisited = True
        for nextPoint in adj[node]:
            if nextPoint[1] not in visited:
                allVisited = False
                break
        if allVisited: continue
        new_cycle = [node]
        current = node
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
cycle = ""
for node in path:
    cycle += node[0]
print(cycle[:-1])