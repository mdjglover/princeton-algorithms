import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    
    private SET<Point2D> pointSet;
    
    public PointSET()
    {
        // construct an empty set of points 
        pointSet = new SET<>();
    }
    
    public boolean isEmpty()
    {
        // is the set empty? 
        return pointSet.isEmpty();
    }
    
    public int size()
    {
        // number of points in the set 
        return pointSet.size();
    }
    
    public void insert(Point2D p)
    {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        
        pointSet.add(p);
    }
    
    public boolean contains(Point2D p)
    {
        // does the set contain point p? 
        if (p == null) throw new IllegalArgumentException();
        
        return pointSet.contains(p);
    }
    
    public void draw()
    {
        // draw all points to standard draw
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize();
        StdDraw.setPenRadius(0.01);
        
        for (Point2D point2d : pointSet)
        {
            StdDraw.point(point2d.x(), point2d.y());
        }
        StdDraw.show();
    }
    
    public Iterable<Point2D> range(RectHV rect)
    {
        // all points that are inside the rectangle (or on the boundary) 
        if (rect == null) throw new IllegalArgumentException();
        
        Queue<Point2D> queue = new Queue<>();
        
        for (Point2D p : pointSet)
        {
            if (rect.contains(p))
                queue.enqueue(p);
        }
       
        return queue;
    }
    
    public Point2D nearest(Point2D p)
    {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) throw new IllegalArgumentException();
        
        Point2D nearest = null;
        
        if (!pointSet.isEmpty())
        {
            for (Point2D point2d : pointSet)
            {
                if (nearest == null || point2d.distanceSquaredTo(p) < nearest.distanceSquaredTo(p))
                {
                    nearest = point2d;
                }
            }
        }
        
        return nearest;
    }

    public static void main(String[] args)
    {
        // unit testing of the methods (optional) 
        if (args.length == 0)
        {
            StdOut.println("No input file");
            return;
        }
        
        In in = new In(args[0]);
        PointSET pSet = new PointSET();
        
        while (!in.isEmpty())
        {
            double x = in.readDouble();
            double y = in.readDouble();
            pSet.insert(new Point2D(x, y));
        }
        
        pSet.draw();
        
        StdOut.println(pSet.nearest(new Point2D(0.5, 0.5)));
    }

}
