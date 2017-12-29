import java.util.Collections;
import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
   private final LineSegment[] lineSegments;
   public FastCollinearPoints(Point[] points) {
      checkNull(points);
      Point[] sortedPoints = points.clone();
      Arrays.sort(sortedPoints);
      checkDuplicate(sortedPoints);
      final int N = points.length;
      LinkedList<LineSegment> listOfSegments = new LinkedList<LineSegment>();
      for (int i = 0; i < N-3; i++) {
         Point origin = sortedPoints[i];
         Point[] pointsSortedBySlope = sortedPoints.clone();
         Arrays.sort(pointsSortedBySlope, origin.slopeOrder());
         double slope = Double.NEGATIVE_INFINITY;
         LinkedList<Point> candidates = new LinkedList<Point>();
         for (int j = 1; j < N; j++) {
            Point point = pointsSortedBySlope[j];
            if (origin.slopeTo(point) == slope) {
               candidates.add(point);
            } else {
               if (candidates.size() >= 3)
                  checkCandidatesAndAddSegment(candidates, origin, listOfSegments);
               candidates = new LinkedList<Point>(Arrays.asList(point));
               slope = origin.slopeTo(point);
            }
         }
         if (candidates.size() >= 3)
            checkCandidatesAndAddSegment(candidates, origin, listOfSegments);
      }
      lineSegments = listOfSegments.toArray(new LineSegment[listOfSegments.size()]);
   }
   private void checkCandidatesAndAddSegment(
      LinkedList<Point> candidates,
      Point origin,
      LinkedList<LineSegment> listOfSegments) {
      if (origin.compareTo(Collections.min(candidates)) > 0)
         return;
      listOfSegments.add(new LineSegment(origin, Collections.max(candidates)));
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
      FastCollinearPoints collinear = new FastCollinearPoints(points);
      for (LineSegment segment : collinear.segments()) {
         StdOut.println(segment);
         segment.draw();
      }
      StdDraw.show();
   }
}