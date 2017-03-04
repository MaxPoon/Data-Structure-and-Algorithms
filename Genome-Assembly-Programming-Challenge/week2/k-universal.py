#Uses python3

from collections import defaultdict   
import itertools

in_binary= int(input())
lpos = in_binary-1

last_binary ='1'*in_binary
bin_int = int(last_binary, 2)

last_before = "1"+('0'*lpos)
first = '0'*in_binary
nodes = defaultdict(list)
for i in range(0,bin_int+1):
		a = (bin(i)[2:].zfill(in_binary))
		if (a!=last_before and a!=first):
			s = a[0:lpos]
			e = a[1:in_binary]
			nodes[s].append(e)
			nodes[e].append(s)

start = '0'*(in_binary-1)
tour = [start]
current = start
while(len(nodes[current])>0):
	suffix = current[1:]
	nextChar = "1" if suffix+"1" in nodes[current] else "0"
	tour.append(suffix+nextChar)
	nodes[current].remove(suffix+nextChar)
	nodes[suffix+nextChar].remove(current)
	current = suffix+nextChar  

ou_res ='0'
for i,d in enumerate(tour):
   ou_res+=d[0]

print(ou_res)