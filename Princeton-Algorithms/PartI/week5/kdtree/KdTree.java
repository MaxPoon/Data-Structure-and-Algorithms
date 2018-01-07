import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {
   private Node root;
   private int size;

   private class Node {
      final Point2D point;
      final double bound;
      Node left;
      Node right;
      boolean isVertical;
      boolean isLeftChild;
      RectHV rect; // the axis-aligned rectangle corresponding to this node

      public Node(Point2D point, double bound, boolean isVertical, boolean isLeftChild) {
         this.point = point;
         this.bound = bound;
         this.isVertical = isVertical;
         this.isLeftChild = isLeftChild;
      }

      public void draw() {
         StdDraw.setPenRadius();
         if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            if (isLeftChild)
               StdDraw.line(point.x(), 0, point.x(), bound);
            else
               StdDraw.line(point.x(), bound, point.x(), 1);
         } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            if (isLeftChild)
               StdDraw.line(0, point.y(), bound, point.y());
            else
               StdDraw.line(bound, point.y(), 1, point.y());
         }
         StdDraw.setPenColor(StdDraw.BLACK);
         StdDraw.setPenRadius(0.01);
         StdDraw.point(point.x(), point.y());
      }
   }
   public KdTree() {
      root = null;
      size = 0;
   }
   public boolean isEmpty() {
      return size == 0;
   }
   public int size() {
      return size;
   }
   public void insert(Point2D p) {
      if (contains(p))
         return;
      size++;
      if (root == null) {
         root = new Node(p, 1, true, true);
         return;
      }
      insert(p, root);
   }
   private void insert(Point2D p, Node node) {
      double pVal, nodeVal;
      if (node.isVertical) {
         pVal = p.x();
         nodeVal = node.point.x();
      } else {
         pVal = p.y();
         nodeVal = node.point.y();
      }
      if (pVal < nodeVal) {
         if (node.left != null) {
            insert(p, node.left);
         } else {
            node.left = new Node(p, nodeVal, !node.isVertical, true);
         }
      } else {
         if (node.right != null) {
            insert(p, node.right);
         } else {
            node.right = new Node(p, nodeVal, !node.isVertical, false);
         }
      }
   }
   public boolean contains(Point2D p) {
      checkNull(p);
      if (isEmpty())
         return false;
      Node node = root;
      double pVal, nodeVal, pVal2, nodeVal2;
      while (node != null) {
         if (node.isVertical) {
            pVal = p.x();
            nodeVal = node.point.x();
            pVal2 = p.y();
            nodeVal2 = node.point.y();
         } else {
            pVal = p.y();
            nodeVal = node.point.y();
            pVal2 = p.x();
            nodeVal2 = node.point.x();
         }
         if (pVal == nodeVal && pVal2 == nodeVal2) {
            return true;
         } else if (pVal < nodeVal) {
            node = node.left;
         } else {
            node = node.right;
         }
      }
      return false;
   }
   public void draw() {
      draw(root);
   }
   private void draw(Node node) {
      if (node == null)
         return;
      draw(node.left);
      draw(node.right);
      node.draw();
   }
   public Iterable<Point2D> range(RectHV rect) {
      checkNull(rect);
      LinkedList<Point2D> result = new LinkedList<Point2D>();
      if (isEmpty())
         return result;
      range(root, rect, result);
      return result;
   }
   private void range(Node node, RectHV rect, LinkedList<Point2D> result) {
      if (node == null)
         return;
      double val, min, max;
      if (node.isVertical) {
         val = node.point.x();
         min = rect.xmin();
         max = rect.xmax();
      } else {
         val = node.point.y();
         min = rect.ymin();
         max = rect.ymax();
      }
      if (min <= val && val <= max) {
         if (rect.contains(node.point))
            result.add(node.point);
         range(node.left, rect, result);
         range(node.right, rect, result);
      } else {
         double nodeVal, rectVal;
         if (node.isVertical) {
            nodeVal = node.point.x();
            rectVal = rect.xmax();
         } else {
            nodeVal = node.point.y();
            rectVal = rect.ymax();
         }
         if (rectVal < nodeVal)
            range(node.left, rect, result);
         else
            range(node.right, rect, result);
      }
   }
   public Point2D nearest(Point2D p) {
      checkNull(p);
      if (isEmpty())
         return null;
      return nearest(p, root);
   }
   private Point2D nearest(Point2D target, Node node) {
      double minDist = node.point.distanceSquaredTo(target);
      Point2D nearestPoint = node.point;
      if (node.left == null && node.right == null)
         return nearestPoint;
      double nodeVal, targetVal;
      if (node.isVertical) {
         nodeVal = node.point.x();
         targetVal = target.x();
      } else {
         nodeVal = node.point.y();
         targetVal = target.y();
      }
      if (targetVal < nodeVal) {
         if (node.left != null) {
            Point2D nearestPointFromLeft = nearest(target, node.left);
            double minDistFromLeft = nearestPointFromLeft.distanceSquaredTo(target);
            if (minDistFromLeft < minDist) {
               minDist = minDistFromLeft;
               nearestPoint = nearestPointFromLeft;
            }
         }
         if (node.right != null) {
            RectHV rightRect = new RectHV(
               (node.isVertical ? node.point.x() : (node.isLeftChild ? 0 : node.bound)),
               (node.isVertical ? (node.isLeftChild ? 0 : node.bound) : node.point.y()),
               (node.isVertical ? 1 : (node.isLeftChild ? node.bound : 1)),
               (node.isVertical ? (node.isLeftChild ? node.bound : 1) : 1)
            );
            if (rightRect.distanceSquaredTo(target) < minDist) {
               Point2D nearestPointFromRight = nearest(target, node.right);
               double minDistFromRight = nearestPointFromRight.distanceSquaredTo(target);
               if (minDistFromRight < minDist) {
                  minDist = minDistFromRight;
                  nearestPoint = nearestPointFromRight;
               }
            }
         }
      } else if (targetVal >= nodeVal) {
         if (node.right != null) {
            Point2D nearestPointFromRight = nearest(target, node.right);
            double minDistFromRight = nearestPointFromRight.distanceSquaredTo(target);
            if (minDistFromRight < minDist) {
               minDist = minDistFromRight;
               nearestPoint = nearestPointFromRight;
            }
         }
         if (node.left != null) {
            RectHV leftRect = new RectHV(
               (node.isVertical ? 0 : (node.isLeftChild ? 0 : node.bound)),
               (node.isVertical ? (node.isLeftChild ? 0 : node.bound) : 0),
               (node.isVertical ? node.point.x() : (node.isLeftChild ? node.bound : 1)),
               (node.isVertical ? (node.isLeftChild ? node.bound : 1) : node.point.y())
            );
            if (leftRect.distanceSquaredTo(target) < minDist) {
               Point2D nearestPointFromLeft = nearest(target, node.left);
               double minDistFromLeft = nearestPointFromLeft.distanceSquaredTo(target);
               if (minDistFromLeft < minDist) {
                  minDist = minDistFromLeft;
                  nearestPoint = nearestPointFromLeft;
               }
            }
         }
      }
      return nearestPoint;
   }
   private void checkNull(Object obj) {
      if (obj == null)
         throw new IllegalArgumentException();
   }
}