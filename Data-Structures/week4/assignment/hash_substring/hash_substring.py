# python3
p = 1000000007 
x = 263
def read_input():
	return (input().rstrip(), input().rstrip())

def print_occurrences(output):
	print(' '.join(map(str, output)))

def get_occurrences(pattern, text):
	output = []
	l = len(pattern)
	hashedValue = sum([ord(pattern[i]) for i in range(len(pattern))])
	currentHashed = sum([ord(text[:l][i]) for i in range(len(text[:l]))])
	if text[:l] == pattern:
		output.append(0)
	for i in range(1,len(text)-l+1):
		currentHashed = currentHashed - ord(text[i-1]) + ord(text[i+l-1])
		if currentHashed == hashedValue:
			if text[i:i+l] == pattern: output.append(i)
	return output



	

if __name__ == '__main__':
	print_occurrences(get_occurrences(*read_input()))

