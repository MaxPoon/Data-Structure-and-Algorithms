#Uses python3

from collections import defaultdict
import collections   
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
tour = []
start = '0'*(in_binary-1)
tour.append(start)
while(len(nodes[start])>0):
	estart = start[1:len(start)]
	if estart+"1" in  nodes[start]:
		tour.append(estart+"1")
		nodes[start].remove(estart+"1")
		nodes[estart+"1"].remove(start)
		start = estart+"1"
	else:
		tour.append(estart+"0")
		nodes[start].remove(estart+"0")
		nodes[estart+"0"].remove(start)
		start = estart+"0"      

ou_res ='0'
for i,d in enumerate(tour):
   ou_res+=d[0]
   
print(ou_res)
