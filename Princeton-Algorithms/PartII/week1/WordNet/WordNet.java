import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {

    private ST<String, Bag<Integer>> words;
    private ST<Integer, String> synset;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Invalid input.");
        words = new ST<>();
        synset = new ST<>();

        In in = new In(synsets);
        String line;
        String[] token;
        int id;
        while (in.hasNextLine()) {
            line = in.readLine();
            token = line.split(",");
            id = Integer.parseInt(token[0]);
            synset.put(id, token[1]);
            for (String word: token[1].split(" "))
                if (!words.contains(word)) {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    words.put(word, bag);
                }
                else {
                    words.get(word).add(id);
                }
        }

        Digraph graph = new Digraph(synset.max() + 1);
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            line = in.readLine();
            token = line.split(",");
            for (int i = 1; i < token.length; i++) {
                graph.addEdge(Integer.parseInt(token[0]), Integer.parseInt(token[i]));
            }
        }

        if (new DirectedCycle(graph).hasCycle()) {
            throw new IllegalArgumentException("Graph has cycles.");
        }

        int rootNumber = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (!graph.adj(i).iterator().hasNext()) {
                rootNumber++;
            }
            if (rootNumber > 1)
                throw new IllegalArgumentException("More than single root");
        }

        sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return words.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Null input.");
        return words.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validate(nounA, nounB);
        Iterable<Integer> a = words.get(nounA);
        Iterable<Integer> b = words.get(nounB);
        return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validate(nounA, nounB);
        Iterable<Integer> a = words.get(nounA);
        Iterable<Integer> b = words.get(nounB);
        return synset.get(sap.ancestor(a, b));
    }

    private void validate(String nounA, String nounB) {
        if (!isNoun(nounA))
            throw new IllegalArgumentException("Word " + nounA + " not part of WordNet.");
        if (!isNoun(nounB))
            throw new IllegalArgumentException("Word " + nounB + " not part of WordNet.");
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}