#Uses python3

import sys

def largest_number(a):
	#write your code here
	res = ""
	newlist = []
	while len(a):
		first = a[0]
		for x in a[1:]:
			if first*len(x)<x*len(first): first = x
		a.remove(first)
		res += first
	return res

if __name__ == '__main__':
	input = sys.stdin.read()
	data = input.split()
	a = data[1:]
	print(largest_number(a))
	
