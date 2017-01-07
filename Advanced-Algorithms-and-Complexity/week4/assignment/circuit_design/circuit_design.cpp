#include <bits/stdc++.h>
using namespace std;

struct Clause {
    int firstVar;
    int secondVar;
};

struct TwoSatisfiability {
    int numVars;
    vector<Clause> clauses;

    TwoSatisfiability(int n, int m) :
        numVars(n),
        clauses(m)
    {  }

    bool isSatisfiable(vector<int>& result) {
        // This solution tries all possible 2^n variable assignments.
        // It is too slow to pass the problem.
        // Implement a more efficient algorithm here.
        for (int mask = 0; mask < (1 << numVars); ++mask) {
            for (int i = 0; i < numVars; ++i) {
                result[i] = (mask >> i) & 1;
            }

            bool formulaIsSatisfied = true;

            for (const Clause& clause: clauses) {
                bool clauseIsSatisfied = false;
                if (result[abs(clause.firstVar) - 1] == (clause.firstVar < 0)) {
                    clauseIsSatisfied = true;
                }
                if (result[abs(clause.secondVar) - 1] == (clause.secondVar < 0)) {
                    clauseIsSatisfied = true;
                }
                if (!clauseIsSatisfied) {
                    formulaIsSatisfied = false;
                    break;
                }
            }

            if (formulaIsSatisfied) {
                return true;
            }
        }
        return false;
    }
};

int main() {
    ios::sync_with_stdio(false);

    int n, m;
    cin >> n >> m;
    TwoSatisfiability twoSat(n, m);
    for (int i = 0; i < m; ++i) {
        cin >> twoSat.clauses[i].firstVar >> twoSat.clauses[i].secondVar;
    }

    vector<int> result(n);
    if (twoSat.isSatisfiable(result)) {
        cout << "SATISFIABLE" << endl;
        for (int i = 1; i <= n; ++i) {
            if (result[i-1]) {
                cout << -i;
            } else {
                cout << i;
            }
            if (i < n) {
                cout << " ";
            } else {
                cout << endl;
            }
        }
    } else {
        cout << "UNSATISFIABLE" << endl;
    }

    return 0;
}
