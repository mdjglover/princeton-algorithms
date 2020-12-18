
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    
    private int numSegments;
    private LineSegment[] lineSegments;
    
    public BruteCollinearPoints(Point[] points)    
    {
        // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException();
            
        numSegments = 0;
        lineSegments = new LineSegment[1];
        for (int p = 0; p < points.length; p++)
        {
            if (points[p] == null) throw new IllegalArgumentException();
            
            for (int q = p + 1; q < points.length; q++)
            {
                if (points[p].compareTo(points[q]) == 0) throw new IllegalArgumentException();
                
                double pToQ = points[p].slopeTo(points[q]);

                for (int r = q + 1; r < points.length; r++)
                {
                    double qToR = points[q].slopeTo(points[r]);
                    
                    if (pToQ == qToR)
                    {
                        for (int s = r + 1; s < points.length; s++)
                        {
                            double rToS = points[r].slopeTo(points[s]);
                            
                            if (pToQ == qToR && qToR == rToS)
                            {
                                Point minPoint = getMinPoint(points[p], points[q], points[r], points[s]);
                                Point maxPoint = getMaxPoint(points[p], points[q], points[r], points[s]);
                                
                                addLineSegment(new LineSegment(minPoint, maxPoint));
                            }
                        }
                    }
                    
                }
            }
        }
    }
    
    private Point getMinPoint(Point p, Point q, Point r, Point s)
    {
        Point minPoint = p;
        
        if (minPoint.compareTo(q) > 0)
            minPoint = q;
        
        if (minPoint.compareTo(r) > 0)
            minPoint = r;
        
        if (minPoint.compareTo(s) > 0)
            minPoint = s;
        
        return minPoint;
    }
    
    private Point getMaxPoint(Point p, Point q, Point r, Point s)
    {
        Point maxPoint = p;
        
        if (maxPoint.compareTo(q) < 0)
            maxPoint = q;
        
        if (maxPoint.compareTo(r) < 0)
            maxPoint = r;
        
        if (maxPoint.compareTo(s) < 0)
            maxPoint = s;
        
        return maxPoint;
    }
    
    private void resizeLineSegmentArray()
    {
        LineSegment[] temp = new LineSegment[lineSegments.length * 2];
        for (int i = 0; i < lineSegments.length; i++)
        {
            temp[i] = lineSegments[i];
        }
        lineSegments = temp;
    }
    
    private void addLineSegment(LineSegment lineSegment)
    {
        for (int i = 0; i < numSegments; i++)
        {
            if (lineSegment == lineSegments[i])
                return;
        }
        
        numSegments += 1;
        if (numSegments > lineSegments.length)
        {
            resizeLineSegmentArray();
        }
        lineSegments[numSegments-1] = lineSegment;
    }
    
    public int numberOfSegments()
    {
        // the number of line segments
        return numSegments;
    }
    
    public LineSegment[] segments()
    {
        // the line segments
        LineSegment[] temp = new LineSegment[numSegments];
        
        for (int i = 0; i < numSegments; i++)
        {
            temp[i] = lineSegments[i];
        }
        return temp;
    }
    
    public static void main(String[] args) 
    {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
