import edu.princeton.cs.algs4.Insertion;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int numSegments = 0;
    private int arrSize = 1;
    private LineSegment[] segments = new LineSegment[arrSize];

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // could we also do a sorting process, where we sort by slope?
        // then we just check for any 4 points in a row which have the same slope?
        if (points == null) {
            throw new IllegalArgumentException(
                    "Argument to BruteCollinearPoints must not be null"
            );
        }
        else if (points[0] == null) {
            throw new IllegalArgumentException("Null point detected");
        }

        // we need to throw an illegal argument exception if the same point is passed twice
        //  create a separate for loop (costs O(N^2)...)
        for (int i = 0; i < (points.length - 1); i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null point detected");
            }

            for (int j = (i + 1); j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException("Null point detected");
                }
                else if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicate points detected");
                }
            }
        }

        // true brute force is O(n^4) in worst case, that's what we are implementing here.
        for (int i = 0; i < (points.length - 3); i++) {
            for (int j = (i + 1); j < (points.length - 2); j++) {
                // calculate slope
                double s1 = points[i].slopeTo(points[j]);

                for (int k = (j + 1); k < (points.length - 1); k++) {
                    // calculate slope
                    double s2 = points[i].slopeTo(points[k]);

                    if (s1 == s2) {
                        // check the final slope
                        for (int m = (k + 1); m < points.length; m++) {
                            double s3 = points[i].slopeTo(points[m]);

                            if (s3 == s1) {
                                // we need to find the start and end of the line segment
                                //  -> use our comparable point w/ an insertion sort
                                Point[] segmentPoints = {
                                        points[i], points[j], points[k], points[m]
                                };
                                addSegment(segmentPoints);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addSegment(Point[] segmentPoints) {
        Insertion.sort(segmentPoints);

        // create the longest line segment
        LineSegment newSegment = new LineSegment(
                segmentPoints[0], segmentPoints[3]
        );

        // how do we add newSegment to the segment array?
        //      we need to look at resizing the array?
        if (numSegments == arrSize) {
            // double array size since array is full
            arrSize = 2 * arrSize;

            // copy old array
            LineSegment[] oldSegments = segments;

            // create new segments
            segments = new LineSegment[arrSize];
            for (int n = 0; n < oldSegments.length; n++) {
                segments[n] = oldSegments[n];
            }
        }

        segments[numSegments] = newSegment;
        numSegments++;
    }

    // number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    public LineSegment[] segments() {
        // create an array of exact length
        LineSegment[] toReturn = new LineSegment[numSegments];
        for (int i = 0; i < numSegments; i++) {
            toReturn[i] = segments[i];
        }
        return toReturn;
    }

    public static void main(String[] args) {
        // lets define some collinear points
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 3);
        Point p3 = new Point(2, 2);
        Point p4 = new Point(2, 5);
        Point p5 = new Point(3, 3);
        Point p0 = new Point(1, 1);
        Point p6 = new Point(4, 4);
        Point[] points = { p0, p1, p2, p3, p4, p5, p6 };

        // now, lets see if we can check the collinearity
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        System.out.println(bcp.numberOfSegments());
        System.out.println(Arrays.toString(bcp.segments()));
    }
}
