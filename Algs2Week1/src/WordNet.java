import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private HashMap<String, Bag<Integer>> nounsTable;
    private HashMap<Integer, String> synsetsTable;
    private SAP sapFinder;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        
        nounsTable = new HashMap<>();
        synsetsTable = new HashMap<>();
        
        In synIn = new In(synsets);
        int V = 0;
        
        while (!synIn.isEmpty()) {
            V += 1;
            String[] line = synIn.readLine().split(",");
            int synsetID = Integer.parseInt(line[0]);
            synsetsTable.put(synsetID, line[1]);
                        
            String[] nouns = line[1].split(" ");
            for (String s : nouns) {                
                if (nounsTable.containsKey(s)) {
                    Bag<Integer> synsetBag = nounsTable.get(s);
                    synsetBag.add(synsetID);
                    nounsTable.put(s, synsetBag);
                }
                else {
                    Bag<Integer> synsetBag = new Bag<>();
                    synsetBag.add(synsetID);
                    nounsTable.put(s, synsetBag);
                }
            }            
        }
        
        Digraph G = new Digraph(V);
        In hyperIn = new In(hypernyms);
        while (!hyperIn.isEmpty()) {
            String[] vertices = hyperIn.readLine().split(",");
            for (int i = 1; i < vertices.length; i++) {
                G.addEdge(Integer.parseInt(vertices[0]), Integer.parseInt(vertices[i]));
            }
        }    
      
        Topological topological = new Topological(G);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException();
        }
        
        sapFinder = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsTable.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        
        return nounsTable.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!nounsTable.containsKey(nounA) || !nounsTable.containsKey(nounB)) throw new IllegalArgumentException();
        
        return sapFinder.length(nounsTable.get(nounA), nounsTable.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!nounsTable.containsKey(nounA) || !nounsTable.containsKey(nounB)) throw new IllegalArgumentException();
        
        return synsetsTable.get(sapFinder.ancestor(nounsTable.get(nounA), nounsTable.get(nounB)));
    }
    

    // do unit testing of this class
    public static void main(String[] args) {
        
    }
}
