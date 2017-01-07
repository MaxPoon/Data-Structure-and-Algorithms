import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SchoolBus {
    private static FastScanner in;
    private static int INF = 1000 * 1000 * 1000;

    public static void main(String[] args) {
        in = new FastScanner();
        try {
            printAnswer(SchoolBus(readData()));
        } catch (IOException exception) {
            System.err.print("Error during reading: " + exception.toString());
        }
    }

    static Answer SchoolBus(int[][] graph) {
        // This solution tries all the possible sequences of stops.
        // It is too slow to pass the problem.
        // Implement a more efficient algorithm here.
        int n = graph.length;
        Integer[] p = new Integer[n];
        for (int i = 0; i < n; ++i)
            p[i] = i;

        class Solver {
            int bestAnswer = INF;
            List<Integer> bestPath;

            public void solve(Integer[] a, int n) {
                if (n == 1) {
                    boolean ok = true;
                    int curSum = 0;
                    for (int i = 1; i < a.length && ok; ++i)
                        if (graph[a[i - 1]][a[i]] == INF)
                            ok = false;
                        else
                            curSum += graph[a[i - 1]][a[i]];
                    if (graph[a[a.length - 1]][a[0]] == INF)
                        ok = false;
                    else
                        curSum += graph[a[a.length - 1]][a[0]];

                    if (ok && curSum < bestAnswer) {
                        bestAnswer = curSum;
                        bestPath = new ArrayList<Integer>(Arrays.asList(a));
                    }
                    return;
                }
                for (int i = 0; i < n; i++) {
                    swap(a, i, n - 1);
                    solve(a, n - 1);
                    swap(a, i, n - 1);
                }
            }

            private void swap(Integer[] a, int i, int j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }
        Solver solver = new Solver();
        solver.solve(p, n);
        if (solver.bestAnswer == INF)
            return new Answer(-1, new ArrayList<>());
        List<Integer> bestPath = solver.bestPath;
        for (int i = 0; i < bestPath.size(); ++i)
            bestPath.set(i, bestPath.get(i) + 1);
        return new Answer(solver.bestAnswer, bestPath);
    }

    private static int[][] readData() throws IOException {
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                graph[i][j] = INF;

        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int weight = in.nextInt();
            graph[u][v] = graph[v][u] = weight;
        }
        return graph;
    }

    private static void printAnswer(final Answer answer) {
        System.out.println(answer.weight);
        if (answer.weight == -1)
            return;
        for (int x : answer.path)
            System.out.print(x + " ");
        System.out.println();
    }

    static class Answer {
        int weight;
        List<Integer> path;

        public Answer(int weight, List<Integer> path) {
            this.weight = weight;
            this.path = path;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

}
