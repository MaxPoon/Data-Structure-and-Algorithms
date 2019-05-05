import edu.princeton.cs.algs4.In;

public class Outcast {

    private final WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = 0;
        int outcastId = 0;
        int dist = 0;
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                dist += net.distance(nouns[i], nouns[j]);
            }
            if (dist > maxDist) {
                maxDist = dist;
                outcastId = i;
            }
        }
        return nouns[outcastId];
    }

    // see test client below
    public static void main(String[] args) {
        String synsets = "./src/assignments/a1_WordNet/wordnet/synsets.txt";
        String hypernyms = "./src/assignments/a1_WordNet/wordnet/hypernyms.txt";
        String outcasts = "./src/assignments/a1_WordNet/wordnet/outcast11.txt";
        WordNet wordnet = new WordNet(synsets, hypernyms);
        Outcast outcast = new Outcast(wordnet);

        System.out.println(outcast.outcast(new In(outcasts).readAllLines()));
    }
}