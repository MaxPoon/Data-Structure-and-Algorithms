import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Digraph G is null");
        digraph = new Digraph(G);
    }

   // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!validV(v) || !validV(w)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(digraph, w);
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(digraph, v);

        return length(vBFS, wBFS);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!validV(v) || !validV(w)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(digraph, w);
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(digraph, v);

        return length(vBFS, wBFS);
    }

    private int length(BreadthFirstDirectedPaths vBFS, BreadthFirstDirectedPaths wBFS) {
        int sap = INFINITY;
        for (int i = 0; i < digraph.V(); i++) {
            if (wBFS.hasPathTo(i) && vBFS.hasPathTo(i))
                if (wBFS.distTo(i) + vBFS.distTo(i) < sap)
                    sap = wBFS.distTo(i) + vBFS.distTo(i);
        }

        if (sap != INFINITY)
            return sap;
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!validV(v) || !validV(w)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(digraph, w);
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(digraph, v);

        return ancestor(vBFS, wBFS);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!validV(v) || !validV(w)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(digraph, w);
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(digraph, v);

        return ancestor(vBFS, wBFS);
    }

    private int ancestor(BreadthFirstDirectedPaths vBFS, BreadthFirstDirectedPaths wBFS) {
        int ancestor = -1;
        int sap = INFINITY;

        for (int i = 0; i < digraph.V(); i++) {
            if (wBFS.hasPathTo(i) && vBFS.hasPathTo(i)) {
                if (wBFS.distTo(i) + vBFS.distTo(i) < sap) {
                    sap = wBFS.distTo(i) + vBFS.distTo(i);
                    ancestor = i;
                }
            }
        }

        return ancestor;
    }

    private boolean validV(Iterable<Integer> v) {
        if (v.iterator().hasNext())
            return v.iterator().next() >= 0 && v.iterator().next() < digraph.V();
        return true;
    }

    private boolean validV(int v) {
        return v >= 0 && v < digraph.V();
    }

   // do unit testing of this class
   public static void main(String[] args) {

   }
}