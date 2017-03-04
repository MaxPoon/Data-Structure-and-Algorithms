#include <iostream>
#include <set>
#include <vector>
#include <map>
#include <unordered_map>

using namespace std;

//#define DEBUG
#define LOOP(i, m) for(int i = 0; i < m; i++)

typedef vector<int> vi;
typedef vector<vector<int>> vvi;

void PrintVectorInt(vi& v) {
  for (auto& elm : v)
    cout << elm << " ";
  cout << endl;
}

class DeBruijnGraph {
public:
  // k-mer size and threshold t
  DeBruijnGraph(int k, int t) : k_(k), t_(t), v_(0) {

  }

  void AddRead(const string& r) {
    LOOP(i, r.size() - k_ + 1) {
      string kmer = r.substr(i, k_);
      if (edgeSet_.find(kmer) == edgeSet_.end()) {
        edgeSet_.insert(kmer);
        edges_.push_back(kmer);
      }
    }
  }

  void BuildDeBruijnGraph() {
#ifdef DEBUG
    for (auto& e : edges_) {
          cout << e << endl;
    }
#endif

    // generate all vertices from kmers
    for (auto edge : edges_) {
      string pre = edge.substr(0, edge.size() - 1);
      string suf = edge.substr(1);

      prefixMap_.insert({pre, v_});

      if (vertixMap_.find(pre) == vertixMap_.end()) {
        vertices_.push_back(pre);
        vertixMap_[pre] = v_++;
      }

      if (vertixMap_.find(suf) == vertixMap_.end()) {
        vertices_.push_back(suf);
        vertixMap_[suf] = v_++;
      }
    }

#ifdef DEBUG
    cout << "Number of edges = " << edges_.size() << " and number of vertices = " << vertices_.size() << endl;
    LOOP(i, v_) {
      cout << i << " : " << vertices_[i] << endl;
    }
#endif

    adjList_.resize(v_);

    LOOP(i, edges_.size()) {
      string pre = edges_[i].substr(0, edges_[i].size() - 1);
      string suf = edges_[i].substr(1);
      int to_vertex_index = vertixMap_[suf];
      int from_vertex_index = vertixMap_[pre];
      adjList_[from_vertex_index].push_back(to_vertex_index);
    }

#ifdef DEBUG
    LOOP(i, adjList_.size()) {
      LOOP(j, adjList_[i].size()) {
        cout << i << "{" << vertices_[i] << "} -> "
          << adjList_[i][j] << "{" << vertices_[adjList_[i][j]] << "}" <<  endl;
      }
    }
#endif
  }

  int CountBubbles() {
    int cnt = 0;
    LOOP(i, adjList_.size()) // go through every vertices
      if (adjList_[i].size() >= 2) // if has more than 2 outgoing vertices
        cnt += CountBubblesFromSource(i, t_);
    return cnt;
  }

private:
  void GetNonOverLappingPaths(vi& path, set<int>& visited, vvi& allPaths, vector<set<int>>& allSets, int l) {
    if (path.size() == l) {
      allPaths.push_back(path);
      allSets.push_back(visited);
      return;
    }

    int s = path.back();

    // nore more, push back and return
    if (adjList_[s].size() == 0) {
        allPaths.push_back(path);
        allSets.push_back(visited);
        return;
    }

    LOOP(i, adjList_[s].size()) {
      int v = adjList_[s][i];

      if (visited.find(v) == visited.end()) {
        visited.insert(v);
        path.push_back(v);
        GetNonOverLappingPaths(path, visited, allPaths, allSets, l);
        path.pop_back();
        visited.erase(v);
      } else {
        // we're visiting a vertex we've previously visited before
        continue;
      }
    }
  }

  string GetPathString(vi& leftPath, vi& rightPath, int merge) {
    string ret;
    for (auto& p : leftPath)
      if (p == merge) {
        ret += to_string(p);
        break;
      }
      else
        ret += to_string(p) + ",";

      ret += ";";

      for (auto& p : rightPath)
        if (p == merge) {
          ret += to_string(p);
          break;
        }
        else
          ret += to_string(p) + ",";

      return ret;
  }


  int CountBubblesFromLeftRightPaths(vector<set<int>>& leftSet, vector<set<int>>& rightSet, vvi& leftPaths, vvi& rightPaths) {
      bool merged;
      set<string> mergePaths;

      for (int i = 0; i < leftSet.size(); i++) {
          auto& s = leftSet[i];
          for (auto& p : rightPaths) {
              merged = false;
              for (auto& v : p) {
                  if (s.find(v) != s.end()) {
#ifdef DEBUG
                      cout << "found merge point " << v << endl;
#endif
                      merged = true;
                      string pathString = GetPathString(leftPaths[i], p, v);
                      mergePaths.insert(pathString);
                      break; // end current path
                  }
              }

              if (merged) continue;
          }
      }

#ifdef DEBUG
      cout << "path strings" << endl;
      for (auto& s : mergePaths)
        cout << s << endl;
#endif

      return mergePaths.size();
  }

  int CountBubblesFromSource(int s, int t) {
    int count = 0;
    vi path;
    vvi leftPaths, rightPaths;
    vector<set<int>> leftSet, rightSet;
    set<int> visited;
    int nPath = adjList_[s].size();

    for (int i = 0; i < (nPath - 1); i++) {
        for (int j = i + 1; j < nPath; j++) {
            path.clear();
            leftPaths.clear();
            visited.clear();
            leftSet.clear();
            path.push_back(adjList_[s][i]);
            visited.insert(adjList_[s][i]);
            GetNonOverLappingPaths(path, visited, leftPaths, leftSet, t);

            path.clear();
            rightPaths.clear();
            visited.clear();
            rightSet.clear();
            path.push_back(adjList_[s][j]);
            visited.insert(adjList_[s][j]);
            GetNonOverLappingPaths(path, visited, rightPaths, rightSet, t);

            count += CountBubblesFromLeftRightPaths(leftSet, rightSet, leftPaths, rightPaths);
        }
    }

    return count;
  }

  // k-mer size and bubble threshold
  int                         k_;
  int                         t_;

  // edkges
  vector<string>              edges_;
  set<string>                 edgeSet_;

  // vertices
  int                         v_; // number of vertices
  vector<string>              vertices_;
  multimap<string, int>       prefixMap_;
  unordered_map<string, int>  vertixMap_; // vertex value to index

  // graph
  vvi                         adjList_;
};

int main(void) {
  int k, t;
  string read;

  cin >> k >> t;
  DeBruijnGraph graph(k, t);
  while (cin >> read) {
    graph.AddRead(read);
  }

  graph.BuildDeBruijnGraph();

  cout << graph.CountBubbles() << endl;

  return 0;
}