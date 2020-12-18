import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private int first;
    private int last;
    private Item[] queue;
    private int n;
    private int capacity;
    
    public RandomizedQueue()                 
    {
        // construct an empty randomized queue
        first = 0;
        last = 0;
        n = 0;
        capacity = 1;
        queue = (Item[]) new Object[capacity];
    }
    
    private void resize(int newCapacity)
    {
        Item[] copy = (Item[]) new Object[newCapacity];
        for (int i = 0; i < n; i++)
        {
            copy[i] = queue[(first + i) % capacity];
        }
        queue = copy;
        first = 0;
        last = n;
        capacity = newCapacity;
    }
    
    private void swap(int x, int y)
    {
        Item temp = queue[x];
        queue[x] = queue[y];
        queue[y] = temp;
    }
    
    private int randomIndex()
    {
        return (first + StdRandom.uniform(0, n)) % capacity;
    }
    
    public boolean isEmpty()                 
    {
        // is the randomized queue empty?
        return n == 0;
    }
    
    public int size()                        
    {
        // return the number of items on the randomized queue
        return n;
    }
    
    public void enqueue(Item item)           
    {
        // add the item
        if (item == null) throw new IllegalArgumentException();
        
        if (n == capacity)
        {
            resize(capacity * 2);
        }
        
        last = (first + n) % capacity;
        queue[last] = item;
        n++;
    }
    
    public Item dequeue()                    
    {
        // remove and return a random item
        if (isEmpty()) throw new NoSuchElementException();
        
        int index = randomIndex();
        swap(first, index);
        
        Item item = queue[first];
        queue[first] = null;
        first = (first + 1) % capacity;
        n--;
        
        if (n <= capacity / 4)
        {
            resize(capacity / 2);
        }
        
        return item;
    }
    
    public Item sample()                     
    {
        // return a random item (but do not remove it)
        if (isEmpty()) throw new NoSuchElementException();
        int index = randomIndex();
        
        return queue[index];
    }
    
    public Iterator<Item> iterator() 
    {
            return new RandomizedQueueIterator();
    }
    
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int[] indexes;
        private int current;
        
        public RandomizedQueueIterator() 
        {
            indexes = new int[n];
            for (int i = 0; i < n; i++)
            {
                indexes[i] = (first + i) % capacity;
            }
            StdRandom.shuffle(indexes);
            current = 0;
        }
        
        @Override
        public boolean hasNext() {
            return current < n;
        }

        @Override
        public Item next() {
            return queue[indexes[current++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        
        
    }
    
    private void TestQueue()
    {
        StdOut.println("First = " + first);
        StdOut.println("Last = " + last);
        StdOut.println("Capacity = " + capacity);
        StdOut.println("Size = " + n);
        int index;
        
        for (int i = 0; i < n; i++)
        {
            index = (first + i) % capacity;
            StdOut.println(queue[index]);
        }
        StdOut.println();
    }
    
    public static void main(String[] args)   // unit testing (optional)
    {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        StdOut.println(q.size());
        
        q.enqueue("Hello");
        q.enqueue("World");
        q.enqueue("hola");
        q.enqueue("mundo");
        q.enqueue("Blah");
        
        q.TestQueue();
        
        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());
        StdOut.println();
        
        q.TestQueue();
        
        q.enqueue("this");
        q.enqueue("is");
        q.enqueue("more");
        
        q.TestQueue();
        
        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());
        StdOut.println();
        
        q.TestQueue();

        
        for (String string : q)
            StdOut.println(string);
        
        StdOut.println();
        
        for (String string : q)
            StdOut.println(string);
            
    }
}
