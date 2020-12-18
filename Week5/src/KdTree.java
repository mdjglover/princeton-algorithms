
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private Node root;
    private int size;
    
    private class Node
    {
        Point2D point;
        Node lb;
        Node rt;
        int level;
        
        public Node(Point2D point2d, int lvl)
        {
            point = point2d;
            level = lvl;
            lb = null;
            rt = null;
        }
    }
    
    public KdTree()
    {
        // construct an empty set of points 
        root = null;
        size = 0;
    }
    
    public boolean isEmpty()
    {
        // is the set empty? 
        return size == 0;
    }
    
    public int size()
    {
        // number of points in the set 
        return size;
    }
    
    public void insert(Point2D p)
    {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        
        root = insert(root, p, 0);
    }
    
    private Node insert(Node x, Point2D p, int level)
    {
        if (x == null) 
        {
            size += 1;
            return new Node(p, level);
        }
        
        if (x.point.equals(p)) 
            return x;
        
        if (level % 2 == 0) // Checks left and right
        {
            if (p.x() <= x.point.x())
                x.lb = insert(x.lb, p, level+1);
            else
                x.rt = insert(x.rt, p, level+1);
        }
        else // Checks up and down
        {
            if (p.y() <= x.point.y())
                x.lb = insert(x.lb, p, level+1);
            else
                x.rt = insert(x.rt, p, level+1);
        }
            
        return x;
    }
    
    public boolean contains(Point2D p)
    {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();
        
        Node pointNode = get(root, p, 0);
        
        return pointNode != null;
    }
    
    private Node get(Node x, Point2D p, int level)
    {
        if (x == null) 
            return null;
        
        if (x.point.equals(p))
            return x;
        
        if (level % 2 == 0) // Check left and right
        {
            if (p.x() <= x.point.x())
                return get(x.lb, p, level+1);
            else
                return get(x.rt, p, level+1);
        }
        else // Check up and down
        {
            if (p.y() <= x.point.y())
                return get(x.lb, p, level+1);
            else
                return get(x.rt, p, level+1);
        }
    }
    
    public void draw()
    {
        // draw all points to standard draw 
        draw(root);
    }
    
    private void draw(Node x)
    {
        if (x == null)
            return;
        
        StdDraw.point(x.point.x(), x.point.y());
        
        draw(x.lb);
        draw(x.rt);
    }
    
    public Iterable<Point2D> range(RectHV rect)
    {
        // all points that are inside the rectangle (or on the boundary) 
        if (rect == null) throw new IllegalArgumentException();
        
        Queue<Point2D> queue = new Queue<>();
        
        range(root, rect, queue, 0);
        
        return queue;
    }
    
    private void range(Node x, RectHV rect, Queue<Point2D> queue, int level)
    {
        if (x == null) 
            return;
        
        if (rect.contains(x.point))
            queue.enqueue(x.point);
        
        if (level % 2 == 0) // Check left and right
        {
            if (x.point.x() >= rect.xmax() || x.point.x() >= rect.xmin())
                range(x.lb, rect, queue, level+1);
            
            if (x.point.x() <= rect.xmax() || x.point.x() <= rect.xmin())
                range(x.rt, rect, queue, level+1);
        }
        else // Check up and down
        {
            if (x.point.y() >= rect.ymax() || x.point.y() >= rect.ymin())
                range(x.lb, rect, queue, level+1);
            
            if (x.point.y() <= rect.ymax() || x.point.y() <= rect.ymin())
                range(x.rt, rect, queue, level+1);
        }
    }
    
    public Point2D nearest(Point2D p)
    {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) throw new IllegalArgumentException();
        
        if (isEmpty())
            return null;
        
        else
            return nearest(root, p, root.point, 0);
    }
    
    private Point2D nearest(Node x, Point2D p, Point2D min, int level)
    {
        if (x == null)
            return min;
        
        if (x.point.distanceSquaredTo(p) < min.distanceSquaredTo(p))
            min = x.point;
        
        if (level % 2 == 0) // Check left and right
        {
            if (p.x() <= min.x())
            {
                min = nearest(x.lb, p, min, level+1);
                Point2D nearestIntersection = new Point2D(x.point.x(), p.y());
                if (p.distanceSquaredTo(nearestIntersection) < p.distanceSquaredTo(min))
                {
                    min = nearest(x.rt, p, min, level+1);
                }   
            }
            else
            {
                min = nearest(x.rt, p, min, level+1);
                Point2D nearestIntersection = new Point2D(x.point.x(), p.y());
                if (p.distanceSquaredTo(nearestIntersection) < p.distanceSquaredTo(min))
                {
                    min = nearest(x.lb, p, min, level+1);
                }
            }
        }
        else // Check up and down
        {
            if (p.y() <= min.y())
            {
                min = nearest(x.lb, p, min, level+1);
                Point2D nearestIntersection = new Point2D(p.x(), x.point.y());
                if (p.distanceSquaredTo(nearestIntersection) < p.distanceSquaredTo(min))
                {
                    min = nearest(x.rt, p, min, level+1);
                }   
            }
            else
            {
                min = nearest(x.rt, p, min, level+1);
                Point2D nearestIntersection = new Point2D(p.x(), x.point.y());
                if (p.distanceSquaredTo(nearestIntersection) < p.distanceSquaredTo(min))
                {
                    min = nearest(x.lb, p, min, level+1);
                }
            }
        }
        
        return min;
    }

    public static void main(String[] args)
    {
        // unit testing of the methods (optional) 
        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(5.0, 5.0);
        tree.insert(p1);
        
        Point2D p2 = new Point2D(4.3, 5.0);
        tree.insert(p2);
        
        Point2D p3 = new Point2D(2.3, 2.0);
        tree.insert(p3);
        
        StdOut.println(tree.contains(p1));
        StdOut.println(tree.contains(p2));
        StdOut.println(tree.contains(p3));
        
        StdOut.println(tree.contains(new Point2D(4.0, 5.0)));
        
        Queue<Point2D> queue = (Queue<Point2D>) tree.range(new RectHV(3.0, 2.0, 6.0, 6.0));
        
        for (Point2D p : queue)
        {
            StdOut.println(p.toString());
        }
        
        Point2D nearestP = new Point2D(4.1, 5.1);
        StdOut.println(tree.nearest(nearestP));
        StdOut.println(tree.size());



    }

}
