import itertools
import os

puzzle=[
    "5***1***4",
    "***2*6***",
    "**8*9*6**",
    "*4*****1*",
    "7*1*8*4*6",
    "*5*****3*",
    "**6*4*1**",
    "***5*2***",
    "2***6***8"
]

clauses = []

digits = range(1, 10)

def varnum(i, j, k):
    assert(i in digits and j in digits and k in digits)
    return 100*i + 10*j + k


def exactly_one_of(literals):
    clauses.append([l for l in literals])

    for pair in itertools.combinations(literals, 2):
        clauses.append([-l for l in pair])


# cell [i,j] contains exactly one digit
for (i, j) in itertools.product(digits, repeat=2):
    exactly_one_of([varnum(i, j, k) for k in digits])

# k appears exactly once in row i
for (i, k) in itertools.product(digits, repeat=2):
    exactly_one_of([varnum(i, j, k) for j in digits])

# k appears exactly once in column j
for (j, k) in itertools.product(digits, repeat=2):
    exactly_one_of([varnum(i, j, k) for i in digits])

# k appears exactly once in a 3x3 block
for (i, j) in itertools.product([1, 4, 7], repeat=2):
    for k in digits:
        exactly_one_of([varnum(i + deltai, j + deltaj, k) for (deltai, deltaj) in itertools.product(range(3), repeat=2)])


for (i, j) in itertools.product(digits, repeat=2):
    if puzzle[i - 1][j - 1] != "*":
        k = int(puzzle[i - 1][j - 1])
        assert(k in digits)
        # [i,j] already contains k:
        clauses.append([varnum(i, j, k)])

with open('tmp.cnf', 'w') as f:
    f.write("p cnf {} {}\n".format(999, len(clauses)))
    for c in clauses:
        c.append(0);
        f.write(" ".join(map(str, c))+"\n")

os.system("minisat tmp.cnf tmp.sat")

with open("tmp.sat", "r") as satfile:
    for line in satfile:
        if line.split()[0] == "UNSAT":
            print("There is no solution")
        elif line.split()[0] == "SAT":
            pass
        else:
            assignment = [int(x) for x in line.split()]

            for i in digits:
                for j in digits:
                    for k in digits:
                        if varnum(i, j, k) in assignment:
                            print(k, end="")
                            break

                print("")


