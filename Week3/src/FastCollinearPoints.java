import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    
    private int numSegments;
    private LineSegment[] lineSegments;
    private Point[] copy;
    
    public FastCollinearPoints(Point[] points)
    {
        // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException();
        
        numSegments = 0;
        lineSegments = new LineSegment[1];
        copy = new Point[points.length];
        
        for (int i = 0; i < points.length; i++) 
        {
            if (points[i] == null) throw new IllegalArgumentException();
            
            copy[i] = points[i];
        }
               
        for (int i = 0; i < copy.length; i++)
        {
            Point base = copy[i];
            Arrays.sort(points, base.slopeOrder());
            
            int lo = 1;
            int hi = 2;
            
            boolean flag = base.compareTo(points[lo]) < 0 ? true : false;
            
            while (hi < points.length)
            {
                if (points[hi].slopeTo(base) == points[lo].slopeTo(base))
                {
                    if (points[hi].compareTo(base) < 0)
                        flag = false;
                }
                else
                {
                    if (flag && hi - lo >= 3)
                    {
                        handleLineSegment(points, base, lo, hi);
                    }
                    
                    lo = hi;
                    flag = base.compareTo(points[lo]) < 0 ? true : false;
                }
                hi++;
            }
            
            if (points[points.length - 1].slopeTo(base) == points[lo].slopeTo(base))
            {
                if (flag && hi - lo >= 3)
                {
                    handleLineSegment(points, base, lo, hi);
                }
            }
            
        }
    }
    
    private void handleLineSegment(Point[] points, Point base, int lo, int hi)
    {
        Point[] subArray = new Point[hi-lo];
        for (int j = lo, k = 0; j < hi; j++, k++)
        {
            subArray[k] = points[j];
        }
        Point maxPoint = getMaxPoint(subArray);
        addLineSegment(new LineSegment(base, maxPoint));
    }
    
//    private Point getMinPoint(Point[] subArray)
//    {
//        if (subArray == null) throw new IllegalArgumentException();
//        
//        Point minPoint = subArray[0];
//        
//        for (int i = 1; i < subArray.length; i++)
//        {
//            if (minPoint.compareTo(subArray[i]) > 0)
//                minPoint = subArray[i];
//        }
//        
//        return minPoint;
//    }
    
    private Point getMaxPoint(Point[] subArray)
    {
        if (subArray == null) throw new IllegalArgumentException();
        
        Point maxPoint = subArray[0];
        
        for (int i = 0; i < subArray.length; i++)
        {
            if (maxPoint.compareTo(subArray[i]) < 0)
                maxPoint = subArray[i];
        }
        
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
            if (lineSegments[i].toString().equals(lineSegment.toString()))
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
