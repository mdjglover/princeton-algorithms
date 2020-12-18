import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    
    public static void main(String[] args)
    {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int n = Integer.valueOf(args[0]);
        
        while (!StdIn.isEmpty())
            queue.enqueue(StdIn.readString());
        
        for (int i = 0; i < n; i++)
            StdOut.println(queue.dequeue());
    }
}
