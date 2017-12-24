# Uses python3
import sys

def binary_search(seq, t):
	min = 0
	max = len(seq) - 1
	while True:
		if max < min:
			return -1
		m = (min + max) // 2
		if seq[m] < t:
			min = m + 1
		elif seq[m] > t:
			max = m - 1
		else:
			return m

if __name__ == '__main__':
	input = sys.stdin.read()
	data = list(map(int, input.split()))
	n = data[0]
	m = data[n + 1]
	a = data[1 : n + 1]
	for x in data[n + 2:]:
		# replace with the call to binary_search when implemented
		print(binary_search(a, x), end = ' ')
