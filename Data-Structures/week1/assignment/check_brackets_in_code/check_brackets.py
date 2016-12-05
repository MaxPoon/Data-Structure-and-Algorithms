# python3

import sys

class Bracket:
    def __init__(self, bracket_type, position):
        self.bracket_type = bracket_type
        self.position = position

    def Match(self, c):
        if self.bracket_type == '[' and c == ']':
            return True
        if self.bracket_type == '{' and c == '}':
            return True
        if self.bracket_type == '(' and c == ')':
            return True
        return False

if __name__ == "__main__":
    text = sys.stdin.read()

    opening_brackets_stack = []
    answer = 0
    for i, c in enumerate(text, start = 1):
        if c == '(' or c == '[' or c == '{':
            # Process opening bracket, write your code here
            opening_brackets_stack.append(Bracket(c, i))

        if c == ')' or c == ']' or c == '}':
            # Process closing bracket, write your code here
            if len(opening_brackets_stack) == 0:
                answer = i
                break
            top = opening_brackets_stack.pop()
            if not top.Match(c): 
            	answer = i
            	break
    # Printing answer, write your code here
    if answer!=0:
    	print(answer)
    elif(len(opening_brackets_stack)):
    	top = opening_brackets_stack.pop()
    	print(top.position)
    else:
    	print("Success")
