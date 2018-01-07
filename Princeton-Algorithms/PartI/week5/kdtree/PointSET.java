import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Comparator;

public class PointSET {
   private final TreeSet<Point2D> points;
   public PointSET() {
      points = new TreeSet<Point2D>();
   }
   public boolean isEmpty() {
      return points.isEmpty();
   }
   public int size() {
      return points.size();
   }
   public void insert(Point2D p) {
      if (contains(p))
         return;
      points.add(p);
   }
   public boolean contains(Point2D p) {
      checkNull(p);
      return points.contains(p);
   }
   public void draw() {
      for (Point2D p: points)
         p.draw();
   }
   public Iterable<Point2D> range(RectHV rect) {
      checkNull(rect);
      LinkedList<Point2D> result = new LinkedList<Point2D>();
      for (Point2D p: points) {
         if (rect.contains(p))
            result.add(p);
      }
      return result;
   }
   public Point2D nearest(Point2D p) {
      checkNull(p);
      if (isEmpty())
         return null;
      return Collections.min(points, new Comparator<Point2D>() {
         @Override
         public int compare(Point2D p1, Point2D p2) {
            double d1 = p1.distanceSquaredTo(p);
            double d2 = p2.distanceSquaredTo(p);
            if (d1 == d2)
               return 0;
            return d1 > d2 ? 1 : -1;
         }
      });
   }
   private void checkNull(Object obj) {
      if (obj == null)
         throw new IllegalArgumentException();
   }
}