/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private Node root;

    public KdTree() {
        // we want to build a generalised Binary Search Tree
        // that is, a BST with points in the nodes of the tree
    }

    private class Node {
        // slightly confused about key and value
        // key needs to be comparable and is how we traverse our search tree
        // value is what we return to the user on a search
        // for our example we want to compare on the point and return it?
        //      I'm removing the node key/value and using only point

        private Point2D point;      // using point instead of key/value
        private Node left, right;   // we set these in put()
        private int n;              // nodes in subtree of this node
        private boolean dir;        // direction of the line (vertical == true)

        // we also need to record the bounds of this point in its given dir
        private double ymax = 1.0;
        private double ymin = 0.0;
        private double xmax = 1.0;
        private double xmin = 0.0;

        public Node(Point2D p, boolean d, int n) {
            this.point = p;
            this.dir = d;
            this.n = n;
        }
    }

    private boolean isVertical(Node x) {
        // not sure what null behaviour we should have (true/false)
        // I've tried to code the solution so we never see node == null here
        if (x == null) return true;
        return x.dir == VERTICAL;
    }

    public boolean isEmpty() {
        return size(root) == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.n;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        root = put(root, p, VERTICAL, null);
    }

    // we need to create a 2D BST
    // that means, we need to add a 'color' or 'direction' to our nodes
    // and based on that direction, choose whether we compare the x or y co-ordinates
    private Node put(Node node, Point2D p, boolean dir, Node parent) {
        // base case (?) if the node is null, it's an empty space, we create a new node
        // because of the code below, it will be inserted to the left, or right of a previous node
        if (node == null) {
            Node newNode = new Node(p, dir, 1);
            if (parent != null) {
                // get the limits from the parent node
                if (dir == VERTICAL) {
                    if (p.y() > parent.point.y()) {
                        newNode.ymax = parent.ymax;
                        newNode.ymin = parent.point.y();
                    }
                    else {
                        newNode.ymax = parent.point.y();
                        newNode.ymin = parent.ymin;
                    }

                    // // set L/R limits
                    // newNode.xmax = parent.xmax;
                    // newNode.xmin = parent.xmin;

                }
                else {
                    if (p.x() > parent.point.x()) {
                        newNode.xmax = parent.xmax;
                        newNode.xmin = parent.point.x();
                    }
                    else {
                        newNode.xmax = parent.point.x();
                        newNode.xmin = parent.xmin;
                    }

                    // // set y limits
                    // newNode.ymax = parent.ymax;
                    // newNode.ymin = parent.ymin;
                }
            }
            return newNode;
        }

        // if x isn't null, we need to decide which way to move in the tree
        int cmp;
        boolean newDir;

        // if the node doesn't already exist, continue
        if (!node.point.equals(p)) {
            if (isVertical(node)) {
                // if we have a vertical node/line, we compare on x co-ordinates
                cmp = Double.compare(p.x(), node.point.x());
                newDir = HORIZONTAL;
            }
            else {
                // if we have a horizontal node/line we compare on y co-ordinates
                cmp = Double.compare(p.y(), node.point.y());
                newDir = VERTICAL;
            }

            // if our point is less than the current node, go left
            if (cmp < 0) {
                node.left = put(node.left, p, newDir, node);
            }

            // if our point is greater, go right
            else {
                node.right = put(node.right, p, newDir, node);
            }

            // now update the size.
            // This happens in ALL cases after the null check
            // we take the size of the left and right subtrees,
            // then add one for the node we are adding with put operation
            node.n = size(node.left) + size(node.right) + 1;
        }

        // then we return the updated node
        // if we didn't return here our changes wouldn't persist into the tree
        return node;
    }

    private Point2D get(Node node, Point2D p) {
        if (node == null) return null;

        else if (node.point.equals(p)) {
            return node.point;
        }

        // if x isn't null, we need to decide which way to move in the tree
        int cmp;

        if (isVertical(node)) {
            // if we have a vertical node/line, we compare on x co-ordinates
            cmp = Double.compare(p.x(), node.point.x());
        }
        else {
            // if we have a horizontal node/line we compare on y co-ordinates
            cmp = Double.compare(p.y(), node.point.y());
        }

        // if our point is less than the current node, go left
        if (cmp < 0) return get(node.left, p);

            // if our point is greater, go right
        else return get(node.right, p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        return get(root, p) != null;
    }

    public void draw() {
        // iterate through the nodes and:
        drawNode(root);
    }

    // function for drawing a node
    private void drawNode(Node node) {
        if (node != null) {
            // 1. draw points in black
            // 2. draw vertical lines in red
            // 3. draw horizontal lines in blue
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            StdDraw.point(node.point.x(), node.point.y());
            StdDraw.setPenRadius(0.005);

            // how do we know the ends of the line
            // I think we need to store that in the Node as well
            //  if we store: limit left, limit right and limit up, limit down
            //      based on whether its vert or horz
            double xmin, xmax, ymin, ymax;
            if (node.dir == VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                xmin = node.point.x();
                xmax = node.point.x();
                ymin = node.ymin;
                ymax = node.ymax;
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                xmin = node.xmin;
                xmax = node.xmax;
                ymin = node.point.y();
                ymax = node.point.y();
            }

            StdDraw.line(xmin, ymin, xmax, ymax);

            // recursively call for children
            drawNode(node.left);
            drawNode(node.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        if (this.isEmpty()) {
            return null;
        }
        
        // iterate through both sides of tree,
        //  pruning when a point isn't in the rectangle
        ArrayList<Point2D> pointArr = new ArrayList<Point2D>();

        // start with search below root
        // NOTE: attempting update in place of pointArr
        pointArr = search(root, rect, pointArr);

        return pointArr;
    }

    // recursive function to search subtrees
    private ArrayList<Point2D> search(Node x, RectHV rect, ArrayList<Point2D> pointArr) {
        // take the point of the node
        Point2D p = x.point;

        // check if the point is in the rectangle now, add to Arr if it is
        if (rect.contains(p)) {
            pointArr.add(p);
        }

        // grab left and right nodes
        Node left = x.left;
        Node right = x.right;

        // check if rect intersects with the rectangle created by the left node
        // construct rectangle from private class attributes
        if (left != null) {
            RectHV leftRect = new RectHV(
                    left.xmin,
                    left.ymin,
                    left.xmax,
                    left.ymax
            );

            // check intersection
            if (rect.intersects(leftRect)) {
                // search left node since there is an intersection
                pointArr = search(left, rect, pointArr);
            }
        }

        if (right != null) {
            RectHV rightRect = new RectHV(
                    right.xmin,
                    right.ymin,
                    right.xmax,
                    right.ymax
            );

            // check intersection
            if (rect.intersects(rightRect)) {
                // search right node
                pointArr = search(right, rect, pointArr);
            }
        }

        return pointArr;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (this.isEmpty()) {
            return null;
        }
        else {
            // we need a recursive search to find nearest point
            // NOTE: closest point is always root to start with
            return searchNearest(root, p, root.point);
        }
    }

    private Point2D searchNearest(Node x, Point2D p, Point2D closestPoint) {
        // compare to closest node
        double closestDist = closestPoint.distanceSquaredTo(p);

        if (p.distanceSquaredTo(x.point) < p.distanceSquaredTo(closestPoint)) {
            closestPoint = x.point;
        }

        Node next = x.left;
        // find the splitting line, and choose the correct side to go down
        if (x.dir == VERTICAL) {
            // we need to check on x co-ordinates
            if (p.x() > x.point.x()) {
                // go down right tree
                next = x.right;
            }
        }
        else {
            if (p.y() > x.point.y()) {
                next = x.right;
            }
        }

        // check if rect intersects with the rectangle created by the left node
        // construct rectangle from private class attributes
        if (next != null) {
            RectHV nextRect = new RectHV(next.xmin, next.ymin, next.xmax, next.ymax);

            double nextRectDist = nextRect.distanceSquaredTo(p);

            // search a subtree if it's closer than the current best
            if (nextRectDist < closestDist) {
                // new closest, search subtrees to see if we can improve
                return searchNearest(next, p, closestPoint);
            }
        }

        return closestPoint;
    }

    public static void main(String[] args) {
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);
        Point2D pn = new Point2D(0.449, 0.242);

        RectHV r = new RectHV(0.0, 0.0, 0.5, 0.5);

        KdTree kdt = new KdTree();
        kdt.insert(p1);
        kdt.insert(p2);
        kdt.insert(p3);
        kdt.insert(p4);
        kdt.insert(p5);

        // kdt.draw();

        // check nearest
        System.out.print("Nearest Point to (0.449, 0.242): ");
        System.out.println(kdt.nearest(pn));

        // check rectangle
        System.out.println("Points within (0.2, 0.0), (0.3, 0.2) rectangle: ");
        for (Point2D q : kdt.range(r)) {
            System.out.println(q.toString());
        }

        // KdTree st1 = new KdTree();
        KdTree st2 = new KdTree();
        // st1.insert(new Point2D(0.75, 0.625));
        // st1.insert(new Point2D(0.0, 0.75));
        // st1.insert(new Point2D(0.5, 0.875));
        // st1.insert(new Point2D(0.125, 0.25));
        // st1.insert(new Point2D(0.25, 0.5));
        // st1.insert(new Point2D(0.75, 0.5));
        // st1.insert(new Point2D(0.625, 0.875));
        // st1.insert(new Point2D(0.125, 1.0));
        // st1.insert(new Point2D(0.5, 0.125));
        // st1.insert(new Point2D(0.25, 0.375));
        st2.insert(new Point2D(0.375, 0.5));
        st2.insert(new Point2D(0.375, 0.875));
        st2.insert(new Point2D(0.375, 0.75));
        st2.insert(new Point2D(0.875, 0.0));
        st2.insert(new Point2D(0.5, 0.125));
        st2.insert(new Point2D(0.75, 0.5));
        st2.insert(new Point2D(0.375, 0.75));
        st2.insert(new Point2D(0.0, 0.5));
        st2.insert(new Point2D(0.125, 0.375));
        st2.insert(new Point2D(0.25, 0.125));

        st2.draw();
        st2.range(new RectHV(0.0, 0.25, 0.375, 0.375));
    }
}
