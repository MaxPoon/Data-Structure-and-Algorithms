import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
   private final LineSegment[] lineSegments;
   public BruteCollinearPoints(Point[] points) {
      checkNull(points);
      Point[] sortedPoints = points.clone();
      Arrays.sort(sortedPoints);
      checkDuplicate(sortedPoints);
      final int N = points.length;
      LinkedList<LineSegment> listOfSegments = new LinkedList<LineSegment>();
      for (int i = 0; i < N-3; i++) {
         Point first = sortedPoints[i];
         for (int j = i+1; j < N-2; j++) {
            Point second = sortedPoints[j];
            double slope1 = first.slopeTo(second);
            for (int k = j+1; k < N-1; k++) {
               Point third = sortedPoints[k];
               double slope2 = first.slopeTo(third);
               if (slope1 == slope2) {
                  for (int l = k+1; l < N; l++) {
                     Point fourth = sortedPoints[l];
                     double slope3 = first.slopeTo(fourth);
                     if (slope1 == slope3) {
                        listOfSegments.add(new LineSegment(first, fourth));
                     }
                  }
               }
            }
         }
      }
      lineSegments = listOfSegments.toArray(new LineSegment[listOfSegments.size()]);
   }
   private void checkNull(Point[] points) {
         if (points == null) {
            throw new IllegalArgumentException("The array \"Points\" is null.");
         }
         for (Point p : points) {
            if (p == null) {
               throw new IllegalArgumentException(
                  "The array \"Points\" contains null element.");
            }
         }
   }
   private void checkDuplicate(Point[] points) {
      for (int i = 0; i < points.length - 1; i++) {
         if (points[i].compareTo(points[i+1]) == 0)
            throw new IllegalArgumentException("Duplicate(s) found.");
      }
   }
   public           int numberOfSegments() {
      return lineSegments.length;
   }
   public LineSegment[] segments() {
      return lineSegments.clone();
   }

   public static void main(String[] args) {

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