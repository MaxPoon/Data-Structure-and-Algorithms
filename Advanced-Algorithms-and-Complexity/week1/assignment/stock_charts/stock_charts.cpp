#include <iostream>
#include <vector>
#include <algorithm>
#include <memory>

using std::vector;
using std::cin;
using std::cout;

class StockCharts {
 public:
  void Solve() {
    vector<vector<int>> stock_data = ReadData();
    int result = MinCharts(stock_data);
    WriteResponse(result);
  }

 private:
  vector<vector<int>> ReadData() {
    int num_stocks, num_points;
    cin >> num_stocks >> num_points;
    vector<vector<int>> stock_data(num_stocks, vector<int>(num_points));
    for (int i = 0; i < num_stocks; ++i)
      for (int j = 0; j < num_points; ++j) {
        cin >> stock_data[i][j];
      }
    return stock_data;
  }

  void WriteResponse(int result) {
    cout << result << "\n";
  }

  int MinCharts(const vector<vector<int>>& stock_data) {
    // Replace this incorrect greedy algorithm with an
    // algorithm that correctly finds the minimum number
    // of charts on which we can put all the stock data
    // without intersections of graphs on one chart.

    int num_stocks = stock_data.size();
    // Vector of charts; each chart is a vector of indices of individual stocks.
    vector<vector<int>> charts;
    for (int i = 0; i < stock_data.size(); ++i) {
      bool added = false;
      for (auto& chart : charts) {
        bool can_add = true;
        for (int index : chart)
          if (!compare(stock_data[i], stock_data[index]) &&
              !compare(stock_data[index], stock_data[i])) {
            can_add = false;
            break;
          }
        if (can_add) {
          chart.push_back(i);
          added = true;
          break;
        }
      }
      if (!added) {
        charts.emplace_back(vector<int>{i});
      }
    }
    return charts.size();
  }

  bool compare(const vector<int>& stock1, const vector<int>& stock2) {
    for (int i = 0; i < stock1.size(); ++i)
      if (stock1[i] >= stock2[i])
        return false;
    return true;
  }
};

int main() {
  std::ios_base::sync_with_stdio(false);
  StockCharts stock_charts;
  stock_charts.Solve();
  return 0;
}
