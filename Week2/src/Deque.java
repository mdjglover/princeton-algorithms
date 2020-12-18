import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item>
{
    private Node first;
    private Node last;
    private int size;
    
    private class Node
    {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    public Deque()
    {
        // construct an empty deque
        first = null;
        last = first;
        size = 0;
    }
    
    public boolean isEmpty()
    {
        // is the deque empty?
        return size == 0;
    }
    
    public int size() 
    {
     // return the number of items on the deque
        return size;
           
    }
    
    public void addFirst(Item item) 
    {
        // add the item to the front
        if (item == null) throw new IllegalArgumentException();
        
        if (isEmpty())
        {
            first = new Node();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        }
        else 
        {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.next = oldfirst;
            first.prev = null;
            oldfirst.prev = first;
        }
       
        size += 1;
    }
    
    public void addLast(Item item) 
    {
        // add the item to the end
        if (item == null) throw new IllegalArgumentException();
        
        if (isEmpty())
        {
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = null;
            first = last;
        }
        else
        {
            Node oldlast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = oldlast;
            oldlast.next = last;
        }
        
        size += 1;
    }
    
    public Item removeFirst() 
    {
        // remove and return the item from the front
        if (isEmpty()) throw new NoSuchElementException();
        
        Item item = first.item;
        if (first.next != null)
        {
            first = first.next;
            first.prev = null;
        }
        else
        {
            first = null;
            last = first;
        }
        
        size -= 1;
        
        return item;
    }
    
    public Item removeLast()
    {
        // remove and return the item from the end
        if (isEmpty()) throw new NoSuchElementException();
        
        Item item = last.item;
        if (last.prev != null)
        {
            last = last.prev;
            last.next = null;
        }
        else
        {
            last = null;
            first = last;
        }
        
        size -= 1;
        
        return item;
    }
    
    public Iterator<Item> iterator() 
    {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item>
    {
        private Node current = first;
        
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
   

    public static void main(String[] args) 
    {
        Deque<String> deque = new Deque<>();
        StdOut.print(deque.size);
        StdOut.println();
        deque.addFirst("What");
        deque.addLast(" is");
        deque.addFirst("...");
        deque.addLast(" up");
        deque.addLast("?");
        for (String string : deque)
            StdOut.print(string);
        StdOut.println();
        StdOut.print(deque.size);
        StdOut.println();

        
        StdOut.print(deque.removeFirst());
        StdOut.println();
        StdOut.print(deque.size);
        StdOut.println();

        StdOut.print(deque.removeLast());
        StdOut.println();
        StdOut.print(deque.size);
        StdOut.println();

        for(String string : deque)
            StdOut.print(string);
        StdOut.println();
        StdOut.print(deque.size);
        StdOut.println();

        while (!deque.isEmpty())
            deque.removeLast();
        StdOut.print(deque.size);
        StdOut.println();
    }

}
