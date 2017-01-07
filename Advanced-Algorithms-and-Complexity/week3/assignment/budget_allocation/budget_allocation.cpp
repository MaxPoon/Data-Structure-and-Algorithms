#include <ios>
#include <iostream>
#include <vector>

using namespace std;

struct ConvertILPToSat {
    vector< vector<int> > A;
    vector<int> b;

    ConvertILPToSat(int n, int m) : A(n, vector<int>(m)), b(n)
    {}

    void printEquisatisfiableSatFormula() {
        // This solution prints a simple satisfiable formula
        // and passes about half of the tests.
        // Change this function to solve the problem.
        cout << "3 2" << endl;
        cout << "1 2 0" << endl;
        cout << "-1 -2 0" << endl;
        cout << "1 -2 0" << endl;
    }
};

int main() {
    ios::sync_with_stdio(false);

    int n, m;
    cin >> n >> m;
    ConvertILPToSat converter(n, m);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        cin >> converter.A[i][j];
      }
    }
    for (int i = 0; i < n; i++) {
      cin >> converter.b[i];
    }

    converter.printEquisatisfiableSatFormula();

    return 0;
}
