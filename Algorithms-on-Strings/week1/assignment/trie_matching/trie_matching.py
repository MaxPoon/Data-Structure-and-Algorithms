# python3
import sys

NA = -1

class Node:
	def __init__ (self):
		self.next = {}
		self.isLeaf = False

def solve (text, n, patterns):
	result = []
	root = Node()
	for pattern in patterns:
		currentNode = root
		for i, c in enumerate(pattern):
			if c not in currentNode.next:
				currentNode.next[c] = Node()
			if i == len(pattern) - 1:
				currentNode.next[c].isLeaf = True
			else:
				currentNode = currentNode.next[c]
	
	for i in range(len(text)):
		index = i
		currentNode = root
		while index < len(text):
			c = text[index]
			if c not in currentNode.next: break
			currentNode = currentNode.next[c]
			if currentNode.isLeaf:
				result.append(i)
				break
			index+=1

	return result

text = sys.stdin.readline ().strip ()
n = int (sys.stdin.readline ().strip ())
patterns = []
for i in range (n):
	patterns += [sys.stdin.readline ().strip ()]

ans = solve (text, n, patterns)

sys.stdout.write (' '.join (map (str, ans)) + '\n')
