import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class Solver {
    private final MinPQ<SearchNode> minPQ;
    private final boolean isSolvable;
    private final SearchNode solutionNode;

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        minPQ = new MinPQ<SearchNode>();
        minPQ.insert(new SearchNode(initial, 0, null));

        while (true) {

            SearchNode currNode = minPQ.delMin();
            Board currBoard = currNode.getBoard();

            if (currBoard.isGoal()) {
                isSolvable = true;
                solutionNode = currNode;
                break;
            }
            if (currBoard.hamming() == 2 && currBoard.twin().isGoal()) {
                isSolvable = false;
                solutionNode = null;
                break;
            }

            // Insert each neighbor except the board of the previous search node
            int moves = currNode.getMoves();

            Board prevBoard = moves > 0 ? currNode.prev.getBoard() : null;

            for (Board nextBoard : currBoard.neighbors()) {
                if (prevBoard != null && nextBoard.equals(prevBoard)) {
                    continue;
                }
                minPQ.insert(new SearchNode(nextBoard, moves + 1, currNode));
            }
        }        
    }
    private class SearchNode implements Comparable<SearchNode> {

        public final SearchNode prev;
        private final Board board;
        private final int moves;
        private final int priority;

        SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.getPriority() - that.getPriority();
        }

        public int getPriority() {
            return priority;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

    }
    public boolean isSolvable() {
        return isSolvable;
    }
    public int moves() {
        return isSolvable() ? solutionNode.getMoves() : -1;
    }
    public Iterable<Board> solution() {
        if (!isSolvable)
            return null;
        LinkedList<Board> solution = new LinkedList<Board>();
        SearchNode node = solutionNode;
        while (node != null) {
            solution.addFirst(node.getBoard());
            node = node.prev;
        }
        return solution;
    }
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}