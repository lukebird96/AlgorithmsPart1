/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    // create empty points tree
    private TreeSet<Point2D> pointsTree;

    public PointSET() {
        pointsTree = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return pointsTree.isEmpty();
    }

    public int size() {
        return pointsTree.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point must not be null");

        // O(log(N)) requirement -> insert point to tree (red-black BST so log(N))
        pointsTree.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point must not be null");

        // O(log(N)) requirement -> native contains in TreeSet
        return pointsTree.contains(p);
    }

    public void draw() {
        for (Point2D p : pointsTree) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle must not be null");

        TreeSet<Point2D> subPoints = new TreeSet<Point2D>();

        // O(N) requirement
        for (Point2D p : pointsTree) {
            if (rect.contains(p)) {
                subPoints.add(p);
            }
        }

        return subPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point p must not be null");
        }

        // O(N) requirement
        // this is brute force, check all ps, return closest one
        Point2D minPoint = null;
        double minDist = Double.POSITIVE_INFINITY;

        for (Point2D q : pointsTree) {
            double distance = p.distanceSquaredTo(q);
            if (distance < minDist) {
                minDist = distance;
                minPoint = q;
            }
        }

        return minPoint;
    }

    public static void main(String[] args) {
        Point2D p1 = new Point2D(0.1, 0.1);
        Point2D p2 = new Point2D(0.2, 0.1);
        Point2D p3 = new Point2D(0.3, 0.1);
        Point2D p4 = new Point2D(0.4, 0.1);
        Point2D pn = new Point2D(0.1, 0.11);

        RectHV r = new RectHV(0.2, 0.0, 0.3, 0.2);

        PointSET ps = new PointSET();
        ps.insert(p1);
        ps.insert(p2);
        ps.insert(p3);
        ps.insert(p4);

        // check nearest
        System.out.print("Nearest Point to (0.1, 0.11): ");
        System.out.println(ps.nearest(pn));

        // check rectangle
        System.out.println("Points within (0.2, 0.0), (0.3, 0.2) rectangle: ");
        for (Point2D q : ps.range(r)) {
            System.out.println(q.toString());
        }
    }
}
