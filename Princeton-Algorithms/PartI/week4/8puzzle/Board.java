import java.util.ArrayList;
public class Board {
    private final int dimension;
    private final int[][] blocks;
    private int blankRow;
    private int blankCol;
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException();
        }
        this.dimension = blocks.length;
        this.blocks = copyOf(blocks);
        for (int i = 0; i < dimension; i++)
            this.blocks[i] = blocks[i].clone();
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (blocks[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                    return;
                }
            }
        }
    }
    public int dimension() {
        return dimension;
    }
    public int hamming() {
        int result = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (row == blankRow && col == blankCol) {
                    continue;
                }
                if (manhattan(row, col) != 0) {
                    result++;
                }
            }
        }
        return result;
    }
    public int manhattan() {
        int result = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (row == blankRow && col == blankCol) {
                    continue;
                }
                result += manhattan(row, col);
            }
        }
        return result;
    }
    private int manhattan(int row, int col) {
        int block = blocks[row][col];
        int expectedRow = (block-1) / dimension;
        int expectedCol = (block-1) % dimension;
        return Math.abs(expectedRow-row) + Math.abs(expectedCol-col);
    }
    public boolean isGoal() {
        return hamming() == 0;
    }
    public Board twin() {
        int[][] cloned = copyOf(blocks);
        if (blankRow != 0) {
            swap(cloned, 0, 0, 0, 1);
        } else {
            swap(cloned, 1, 0, 1, 1);
        }
        return new Board(cloned);
    }
    private void swap(int[][] v, int rowA, int colA, int rowB, int colB) {
        int swap = v[rowA][colA];
        v[rowA][colA] = v[rowB][colB];
        v[rowB][colB] = swap;
    }
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board that = (Board) y;
        if (this.dimension != that.dimension)
            return false;
        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if (this.blocks[row][col] != that.blocks[row][col])
                    return false;
        return true;
    }
    private int[][] copyOf(int[][] matrix) {
        int[][] clone = new int[matrix.length][];
        for (int row = 0; row < matrix.length; row++) {
            clone[row] = matrix[row].clone();
        }
        return clone;
    }
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>(4);

        if (blankRow > 0) {
            int[][] north = copyOf(blocks);
            swap(north, blankRow, blankCol, blankRow - 1, blankCol);
            neighbors.add(new Board(north));
        }
        if (blankRow < dimension - 1) {
            int[][] south = copyOf(blocks);
            swap(south, blankRow, blankCol, blankRow + 1, blankCol);
            neighbors.add(new Board(south));
        }
        if (blankCol > 0) {
            int[][] west = copyOf(blocks);
            swap(west, blankRow, blankCol, blankRow, blankCol - 1);
            neighbors.add(new Board(west));
        }
        if (blankCol < dimension - 1) {
            int[][] east = copyOf(blocks);
            swap(east, blankRow, blankCol, blankRow, blankCol + 1);
            neighbors.add(new Board(east));
        }
        return neighbors;
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(dimension).append("\n");
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                builder.append(String.format("%2d ", blocks[row][col]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}