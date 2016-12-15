# python3
import sys

def BWT(text):
	l = len(text)
	M = []
	for i in range(l):
		M.append(text[i:]+text[:i])
	M.sort()
	result = ""
	for row in M:
		result += row[-1]
	return result

if __name__ == '__main__':
	text = sys.stdin.readline().strip()
	print(BWT(text))