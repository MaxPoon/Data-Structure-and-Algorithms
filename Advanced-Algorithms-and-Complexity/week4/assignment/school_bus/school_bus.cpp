#include <iostream>
#include <algorithm>
#include <vector>

using std::vector;
typedef vector<vector<int> > Matrix;

const int INF = 1e9;

Matrix read_data() {
    int vertex_count, edge_count;
    std::cin >> vertex_count >> edge_count;
    Matrix graph(vertex_count, vector<int>(vertex_count, INF));
    for (int i = 0; i < edge_count; ++i) {
        int from, to, weight;
        std::cin >> from >> to >> weight;
        --from, --to;
        graph[from][to] = graph[to][from] = weight;
    }
    return graph;
}

std::pair<int, vector<int> > optimal_path(const Matrix& graph) {
    // This solution tries all the possible sequences of stops.
    // It is too slow to pass the problem.
    // Implement a more efficient algorithm here.
    size_t n = graph.size();
    vector<int> p(n);
    for (size_t i = 0; i < n; ++i)
        p[i] = i;

    vector<int> best_path;
    int best_ans = INF;

    do {
        int cur_sum = 0;
        bool ok = true;
        for (size_t i = 1; i < n && ok; ++i)
            if (graph[p[i - 1]][p[i]] == INF)
                ok = false;
            else
                cur_sum += graph[p[i - 1]][p[i]];
        if (graph[p.back()][p[0]] == INF)
            ok = false;
        else
            cur_sum += graph[p.back()][p[0]];

        if (!ok)
            continue;
        if (cur_sum < best_ans) {
            best_ans = cur_sum;
            best_path = p;
        }
    } while (std::next_permutation(p.begin(), p.end()));

    if (best_ans == INF)
        best_ans = -1;
    for (size_t i = 0; i < best_path.size(); ++i)
        ++best_path[i];
    return std::make_pair(best_ans, best_path);
}

void print_answer(const std::pair<int, vector<int> >& answer) {
    std::cout << answer.first << "\n";
    if (answer.first == -1)
        return;
    const vector<int>& path = answer.second;
    for (size_t i = 0; i < path.size(); ++i)
        std::cout << path[i] << " ";
    std::cout << "\n";
}

int main() {
    print_answer(optimal_path(read_data()));
    return 0;
}
