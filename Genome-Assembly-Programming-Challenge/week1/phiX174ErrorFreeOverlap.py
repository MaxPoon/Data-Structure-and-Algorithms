# Uses python3
DEFAULT_READS_NUMBER = 1618
DEFAULT_MIN_OVERLAP_LENGTH = 70
LENGTH_OF_READ = 100

class TrieNode(object):
	def __init__(self):
		self.children = {}
		self.indexes = []

class PrefixTrie(object):
	def __init__(self):
		self.root = TrieNode()

	def addPrefix(self, string, index):
		for end in range(DEFAULT_MIN_OVERLAP_LENGTH, len(string)):
			reversed_prefix = string[:end][::-1]
			node = self.root
			for char in reversed_prefix:
				if char not in node.children:
					node.children[char] = TrieNode()
				node = node.children[char]
			node.indexes.append(index)

	def match(self, string):
		adjacent = []
		node = self.root
		length = 0
		for char in string[::-1]:
			if char not in node.children:
				break
			node = node.children[char]
			length += 1
			if length >= DEFAULT_MIN_OVERLAP_LENGTH and node.indexes:
				for index in node.indexes:
					adjacent.append((index, length))
		return adjacent

def stringsOverlapValue(s,t):
	for i in range(LENGTH_OF_READ, 0, -1):
		if s[LENGTH_OF_READ-i:] == t[:i]: return i
	return 0

def generateOverlapGraph(reads):
	prefixTrie = PrefixTrie()
	for i, read in enumerate(reads):
		prefixTrie.addPrefix(read, i)
	adj = [[] for _ in range(len(reads))]
	for i, read in enumerate(reads):
		adj[i] = prefixTrie.match(read)
	for l in adj:
		l.sort(key=lambda x: x[1], reverse=True)
	return adj 

def buildLongestHamiltonianPath(adj):
	current = 0
	added = set([0])
	path = [(0, 0)]
	while len(added)<len(adj):
		for i, link in enumerate(adj[current]):
			if link[0] not in added:
				added.add(link[0])
				current = link[0]
				path.append(link)
				break
	return path

def assemble(path, reads):
	genome = ""
	for node in path:
		genome += reads[node[0]][node[1]:]
	genome = genome[:-stringsOverlapValue(reads[path[-1][0]], reads[0])]
	return genome

reads = []
for i in range(DEFAULT_READS_NUMBER):
	reads.append(input())
reads = list(set(reads))
adj = generateOverlapGraph(reads)
path = buildLongestHamiltonianPath(adj)
genome = assemble(path, reads)
print(genome)