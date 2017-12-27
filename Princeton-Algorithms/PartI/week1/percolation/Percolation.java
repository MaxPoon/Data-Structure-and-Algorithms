import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
   private int size;
   private boolean[][] opened;
   private WeightedQuickUnionUF weightedQuickUnionUF;
   private int top;
   private int bottom;
   private int openedSites;
   public Percolation(int n) {
      if (n <= 0)
         throw new IllegalArgumentException("n smaller than 0");
      weightedQuickUnionUF = new WeightedQuickUnionUF(n*n+2);
      top = 0;
      bottom = n*n+1;
      opened = new boolean[n][n];
      openedSites = 0;
      size = n;
      for(int i=1; i <= n; i++) {
         weightedQuickUnionUF.union(top, getQFIndex(1, i));
         weightedQuickUnionUF.union(bottom, getQFIndex(n, i));
      }
   }
   public    void open(int row, int col) {
      if (!isValid(row, col))
         throw new IllegalArgumentException("Index out of bound");
      if (isOpen(row, col)) return;
      opened[row-1][col-1] = true;
      openedSites++;
      int index = getQFIndex(row, col);
      if (row-1 > 0 && isOpen(row-1, col))
         weightedQuickUnionUF.union(index, getQFIndex(row-1, col));
      if (row+1 <= size && isOpen(row+1, col))
         weightedQuickUnionUF.union(index, getQFIndex(row+1, col));
      if (col-1 > 0 && isOpen(row, col-1))
         weightedQuickUnionUF.union(index, getQFIndex(row, col-1));
      if (col+1 <= size && isOpen(row, col+1))
         weightedQuickUnionUF.union(index, getQFIndex(row, col+1));
   }
   public boolean isOpen(int row, int col) {
      if (!isValid(row, col))
         throw new IllegalArgumentException("Index out of bound");
      return opened[row-1][col-1];
   }
   public boolean isFull(int row, int col) {
      if (!isValid(row, col))
         throw new IllegalArgumentException("Index out of bound");
      return weightedQuickUnionUF.connected(top, getQFIndex(row, col));
   }
   public     int numberOfOpenSites() {
      return openedSites;
   }
   public boolean percolates() {
      return weightedQuickUnionUF.connected(top, bottom);
   }
   private int getQFIndex(int i, int j) {
      return size * (i - 1) + j;
   }
   private boolean isValid(int i, int j) {
      return 1 <= i && i <= size && 1 <= j && j <= size;
   }
}