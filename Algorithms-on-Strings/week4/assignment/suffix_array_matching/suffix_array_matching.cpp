#include <cstdio>
#include <iostream>
#include <string>
#include <vector>

using std::cin;
using std::string;
using std::vector;

vector<int> BuildSuffixArray(const string& text) {
  vector<int> order;

  // write your code here

  return order;
}


vector<int> FindOccurrences(const string& pattern, const string& text, const vector<int>& suffix_array) {
  vector<int> result;

  // write your code here

  return result;
}

int main() {
  char buffer[100001];
  scanf("%s", buffer);
  string text = buffer;
  text += '$';
  vector<int> suffix_array = BuildSuffixArray(text);
  int pattern_count;
  scanf("%d", &pattern_count);
  vector<bool> occurs(text.length(), false);
  for (int pattern_index = 0; pattern_index < pattern_count; ++pattern_index) {
    scanf("%s", buffer);
    string pattern = buffer;
    vector<int> occurrences = FindOccurrences(pattern, text, suffix_array);
    for (int j = 0; j < occurrences.size(); ++j) {
      occurs[occurrences[j]] = true;
    }
  }
  for (int i = 0; i < occurs.size(); ++i) {
    if (occurs[i]) {
      printf("%d ", i);
    }
  }
  printf("\n");
  return 0;
}
