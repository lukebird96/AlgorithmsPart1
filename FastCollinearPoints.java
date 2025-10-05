import edu.princeton.cs.algs4.Insertion;

import java.util.Arrays;
import java.util.Objects;

public class FastCollinearPoints {
    private int numSegments = 0;
    // private int numPoints = 0;
    private int arrSize = 1;
    private LineSegment[] segments = new LineSegment[arrSize];
    // private Point[][] allSegmentPoints = { { } };

    public FastCollinearPoints(Point[] points) {
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

        // create a copy of points so we can sort this version of the array
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsCopy[i] = points[i];
        }

        // optimised solution
        // iterate through points
        for (int i = 0; i < points.length; i++) {
            // set an 'origin' point
            Point p0 = points[i];

            // now we check the slope to every other point and sort the array
            //  we need to use merge sort not insertion sort O(nlog(n)) vs O(n^2)
            //  Java uses mergesort for objects because mergesort is stable O(nlog(n))
            //  Java uses quicksort for primitives because it's a faster O(nlog(n))
            //      and stability doesn't matter for primitive data types
            Arrays.sort(pointsCopy, p0.slopeOrder());

            // create an array of slopes
            double[] slopes = new double[points.length];
            for (int j = 0; j < slopes.length; j++) {
                slopes[j] = p0.slopeTo(pointsCopy[j]);
            }

            // define counters
            int j = 0;
            int k = 0;

            // while k is less than the length of slopes
            while (k < slopes.length) {
                // if (k == slopes.length) {
                //     // we have to stop iterating now
                // }
                if (slopes[j] == slopes[k]) {
                    // we have a match AND k is not the last item
                    // we should progress our upper bound
                    k++;
                }
                else if (k - j >= 3) {
                    // we have had at least 3 collinear points - record a segment
                    // this point reference could be wrong
                    //  we have found k and j from the slopes array
                    //  - that wouldn't map directly to points array
                    //  - on the other hand, we are sorting by slope order to p0
                    Point[] segmentPoints = new Point[(k - j) + 1];

                    for (int m = 0; m < (k - j); m++) {
                        segmentPoints[m] = pointsCopy[j + m];
                    }

                    // we need to add our source point on here
                    segmentPoints[segmentPoints.length - 1] = p0;

                    // add segment to our saved array
                    addSegment(segmentPoints);

                    // increment the counters
                    // set j to k's position in case it has moved multiple times
                    j = k;

                    // now move k to +1
                    k++;
                }
                else {
                    // we don't have a match, increment both k and j
                    // set j to k's position in case it has moved multiple times
                    j = k;

                    // now move k to +1
                    k++;
                }
            }

            // we need to catch things from the end of the list here
            if (k - j >= 3) {
                // this code is same as above - should be a function
                //      or implementation should be better
                // we have had at least 3 collinear points - record a segment
                Point[] segmentPoints = new Point[(k - j) + 1];

                for (int m = 0; m < (k - j); m++) {
                    segmentPoints[m] = pointsCopy[j + m];
                }

                // we need to add our source point on here
                segmentPoints[segmentPoints.length - 1] = p0;

                // add segment to our saved array
                addSegment(segmentPoints);
            }
        }
    }

    private void addSegment(Point[] points) {
        // fast sort to get natural order
        Insertion.sort(points);

        // create the longest line segment
        LineSegment newSegment = new LineSegment(
                points[0], points[points.length - 1]
        );

        // if the segment doesn't already exist
        if (validSegment(newSegment)) {
            // // add points to our segment
            // for (int i = 0; i < points.length; i++) {
            //
            //     // resize if needed
            //     if (numPoints + i >= allSegmentPoints.length) {
            //         // double array size
            //         Point[][] oldPoints = allSegmentPoints;
            //         allSegmentPoints = new Point[][] { new Point[2 * (numPoints + i)] };
            //         for (int j = 0; j < oldPoints.length; j++) {
            //             allSegmentPoints[j] = oldPoints[j];
            //         }
            //     }
            //
            //     allSegmentPoints[numPoints + i] = points;
            // }

            // update how many points we have
            // numPoints += points.length;

            // look at resizing the array
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
    }

    private boolean validSegment(LineSegment seg) {
        // we need to check if the segment already exists
        // O(M^2) cost but where m is small on average
        // this is a bug - it's rejecting valid line segments
        //  because the points exist elsewhere
        // the challenge here is: how do we implement a lineSegment check

        // check p0
        // Point p = points[0];
        // Point q = points[points.length - 1];

        for (LineSegment savedSegment : segments) {
            if (savedSegment == null) {
                return true;
            }
            else if (Objects.equals(seg.toString(), savedSegment.toString())) {
                return false;
            }
        }
        return true;
    }

    // implement with generics (how?)
    // private void resizeArray(Point[] array, int size) {
    // }

    public int numberOfSegments() {
        return numSegments;
    }

    // TODO: solve bug by sorting the intial array?
    //      it has to be related to some kind of sorting and moving N steps through

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
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        System.out.print("Number of Segments: ");
        System.out.println(fcp.numberOfSegments());
        System.out.println(Arrays.toString(fcp.segments()));
    }
}
