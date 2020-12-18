import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    private final Digraph digraph;
    private Set<Set<Set<Integer>>> searched;
    private HashMap<Set<Set<Integer>>, Integer> foundLengths;
    private HashMap<Set<Set<Integer>>, Integer> foundAncestors;
    
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        
        digraph = G;
        searched = new HashSet<>();
        foundLengths = new HashMap<>();
        foundAncestors = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {        
        if (v == w) {
            if (v >= digraph.V()) throw new IllegalArgumentException();
            return 0;
        }
        
        Set<Integer> vSet = new HashSet<>();
        vSet.add(v);
        
        Set<Integer> wSet = new HashSet<>();
        wSet.add(w);
        
        Set<Set<Integer>> combinedSet = new HashSet<Set<Integer>>();
        combinedSet.add(vSet);
        combinedSet.add(wSet);
        
        if (searched.contains(combinedSet))
        {
            return foundLengths.get(combinedSet);
        }
        
        findSAP(vSet, wSet, combinedSet);
        
        return foundLengths.get(combinedSet);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v == w) {
            if (v >= digraph.V()) throw new IllegalArgumentException();
            return v;
        }
        
        Set<Integer> vSet = new HashSet<>();
        vSet.add(v);
        
        Set<Integer> wSet = new HashSet<>();
        wSet.add(w);
        
        Set<Set<Integer>> combinedSet = new HashSet<Set<Integer>>();
        combinedSet.add(vSet);
        combinedSet.add(wSet);
        
        if (searched.contains(combinedSet))
        {
            return foundAncestors.get(combinedSet);
        }
        
        findSAP(vSet, wSet, combinedSet);
        
        return foundAncestors.get(combinedSet);
        
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        
        Set<Integer> vSet = new HashSet<>();
        for (int i : v) {
            vSet.add(i);
        }
        
        Set<Integer> wSet = new HashSet<>();
        for (int i : w) { 
            if (vSet.contains(i)) {
                if (i >= digraph.V()) throw new IllegalArgumentException();

                return 0;
            }
            wSet.add(i);
        }
        
        Set<Set<Integer>> combinedSet = new HashSet<Set<Integer>>();
        combinedSet.add(vSet);
        combinedSet.add(wSet);        
        
        if (searched.contains(combinedSet))
        {
            return foundLengths.get(combinedSet);
        }
        
        findSAP(vSet, wSet, combinedSet);
        
        return foundLengths.get(combinedSet);
        
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        
        Set<Integer> vSet = new HashSet<>();
        for (int i : v) {
            vSet.add(i);
        }
        
        Set<Integer> wSet = new HashSet<>();
        for (int i : w) { 
            if (vSet.contains(i)) {
                if (i >= digraph.V()) throw new IllegalArgumentException();

                return i;
            }
            wSet.add(i);
        }
                
        Set<Set<Integer>> combinedSet = new HashSet<Set<Integer>>();
        combinedSet.add(vSet);
        combinedSet.add(wSet);  
        
        if (searched.contains(combinedSet))
        {
            return foundAncestors.get(combinedSet);
        }
        
        
        findSAP(vSet, wSet, combinedSet);
        
        return foundAncestors.get(combinedSet);    
    }
    
    private void findSAP(Set<Integer> v, Set<Integer> w, Set<Set<Integer>> combinedSet) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        
        searched.add(combinedSet);
        
        boolean sapFound = false;
        int minDistance = -1;
        int ancestor = -1;
        
        for (Integer i : w) {
            if (bfsV.hasPathTo(i)) {
                sapFound = true;
                if (minDistance == -1) {
                    minDistance = bfsV.distTo(i);
                    ancestor = i;
                }
                else {
                    if (bfsV.distTo(i) < minDistance) {
                        minDistance = bfsV.distTo(i);
                        ancestor = i;
                    }
                }
            }
        }
        
        if (sapFound) {
            foundAncestors.put(combinedSet, ancestor);
            foundLengths.put(combinedSet, bfsV.distTo(ancestor));
            return;
        }
        
        Queue<Integer> queue = new Queue<>();
        boolean[] marked = new boolean[digraph.V()];
        int[] dist = new int[digraph.V()];
        int[] edgeTo = new int[digraph.V()];
        
        for (Integer i : w) {
            marked[i] = true;
            dist[i] = 0;
            edgeTo[i] = i;
            
            for (Integer j : digraph.adj(i)) {
                if (marked[j]) {
                    continue;
                }
                
                edgeTo[j] = i;
                dist[j] = dist[i] + 1;
                
                queue.enqueue(j);
            }
        }
        
        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            
            if (bfsV.hasPathTo(current)) {
                foundAncestors.put(combinedSet, current);
                foundLengths.put(combinedSet, dist[current] + bfsV.distTo(current));
                sapFound = true;
                break;
            }
            
            for (Integer j : digraph.adj(current)) {
                if (marked[j]) {
                    continue;
                }
                
                edgeTo[j] = current;
                dist[j] = dist[current] + 1;
                
                queue.enqueue(j);
            }
        }
        
        if (!sapFound) {
            foundAncestors.put(combinedSet, -1);
            foundLengths.put(combinedSet, -1);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
