import edu.princeton.cs.algs4.Insertion;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    // constructs the point
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point (not sure what this means)
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int compareTo(Point that) {
        if (this.y > that.y) return 1;
        else if (this.y < that.y) return -1;
        else if (this.x > that.x) return 1;
        else if (this.x < that.x) return -1;
        else return 0;
    }

    public double slopeTo(Point that) {
        double slope;
        if (that.x == this.x) {
            // additional check for matching y values
            if (that.y == this.y) {
                slope = Double.NEGATIVE_INFINITY;
            }
            else {
                slope = Double.POSITIVE_INFINITY;
            }
        }
        else if (that.y == this.y) {
            // worth doing it this way to avoid floating point errors
            slope = (double) 0;
        }
        else {
            slope = (double) (that.y - this.y) / (that.x - this.x);
        }
        return slope;
    }

    // compares two points by the slope they make with this point
    public Comparator<Point> slopeOrder() {
        // check the slope from THIS point to the two points a and b
        return new SlopeOrder();
    }

    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            // we can reference our parent class with it's fully qualified name
            double slp1 = Point.this.slopeTo(p1);
            double slp2 = Point.this.slopeTo(p2);

            // do comparison
            if (slp1 > slp2) return 1;
            else if (slp1 < slp2) return -1;
            return 0;
        }
    }

    public static void main(String[] args) {
        Point p1 = new Point(9, 1);
        Point p2 = new Point(3, 2);
        Point p3 = new Point(2, 2);
        Point p4 = new Point(3, 3);

        // create array of points
        Point[] points = { p1, p2, p3 };

        // print order
        System.out.println("Starting Order: ");
        for (Point p : points) {
            System.out.println(p.toString());
        }

        // test sort
        Insertion.sort(points);

        // print order
        System.out.println("\nSorted Order: ");
        for (Point p : points) {
            System.out.println(p.toString());
        }

        // test slope
        System.out.println("\nCalculating Slopes: ");
        System.out.println(p1.slopeTo(p2));
        System.out.println(p1.slopeTo(p3));
        assert p3.slopeTo(p4) == 1;

        // test sort with slope
        Insertion.sort(points, p1.slopeOrder());

        System.out.println("\nSorted By Slope to p1: ");
        for (Point p : points) {
            System.out.println(p.toString());
        }

        // test sort with slope to p2
        Insertion.sort(points, p2.slopeOrder());

        System.out.println("\nSorted By Slope to p2: ");
        for (Point p : points) {
            System.out.println(p.toString());
        }
    }
}
