#include <iostream>
#include <vector>
#include <string>
using namespace std;

/*
  Arguments:
    * `n` - the number of vertices.
    * `edges` - list of edges, each edge is a pair (u, v), 1 <= u, v <= n.
    * `colors` - string consisting of `n` characters, each belonging to the set {'R', 'G', 'B'}.
  Return value:
    * If there exists a proper recoloring, return value is a string containing new colors, similar to the `colors` argument.
    * Otherwise, return value is an empty string.
*/
string assign_new_colors(int n, vector<pair<int, int>> edges, string colors) {
    // Insert your code here.
    if (n % 3 == 0) {
        string new_colors;
        for (int i = 0; i < n; i++) {
            new_colors.push_back("RGB"[i % 3]);
        }
        return new_colors;
    } else {
        return "";
    }
}

int main() {
    int n, m;
    cin >> n >> m;
    string colors;
    cin >> colors;
    vector<pair<int, int> > edges;
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        edges.push_back(make_pair(u, v));
    }
    string new_colors = assign_new_colors(n, edges, colors);
    if (new_colors.empty()) {
        cout << "Impossible";
    } else {
        cout << new_colors << endl;
    }
}
