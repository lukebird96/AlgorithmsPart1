import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {
    private SearchNode minNode;
    private boolean solvable = true;
    private int moves = 0;

    // create solution stack
    private Stack<Board> solutionStack = new Stack<Board>();

    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previous;

        public SearchNode(Board b, int m, SearchNode p) {
            board = b;
            moves = m;
            previous = p;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial Board cannot be null");
        }

        // to account for unsolvable boards we need to use this twin
        // the twin should complete all the same steps as the initial board
        Board twin = initial.twin();

        // create initial nodes
        SearchNode n = new SearchNode(initial, moves, null);
        SearchNode twinN = new SearchNode(twin, moves, null);

        // create minPQ for initial and twin nodes
        MinPQ<SearchNode> nodeTree = new MinPQ<SearchNode>(
                this.manhattanComparator()
        );
        MinPQ<SearchNode> twinTree = new MinPQ<SearchNode>(
                this.manhattanComparator()
        );

        // add node to tree
        nodeTree.insert(n);
        twinTree.insert(twinN);

        while (true) {
            // delete the minimum PRIORITY now
            minNode = nodeTree.delMin();
            SearchNode twinMinNode = twinTree.delMin();

            if (minNode.board.isGoal()) {
                moves = minNode.moves;
                break;
            }
            else if (twinMinNode.board.isGoal()) {
                // if we hit goal on our twin our board cannot be solved
                solvable = false;
                moves = -1;
                break;
            }

            // get neighbors
            Iterable<Board> neighborsItr = minNode.board.neighbors();
            Iterable<Board> twinNeighborsItr = twinMinNode.board.neighbors();

            // iterate through and add to PQ
            for (Board b : neighborsItr) {
                // update moves based on the parent node
                moves = minNode.moves + 1;

                // create new search node based on the board
                n = new SearchNode(b, moves, minNode);

                // if the board is equal to previousNode we shouldn't add it
                if (minNode.previous == null) nodeTree.insert(n);
                else {
                    if (!n.board.equals(minNode.previous.board)) {
                        nodeTree.insert(n);
                    }
                }
            }

            // iterate through and add to PQ
            for (Board b : twinNeighborsItr) {
                n = new SearchNode(b, moves, twinMinNode);

                // if the board is equal to previousNode we shouldn't add it
                if (twinMinNode.previous == null) twinTree.insert(n);
                else {
                    if (!n.board.equals(twinMinNode.previous.board)) {
                        twinTree.insert(n);
                    }
                }
            }
        }

    }

    // we need to create a comparator on Board?
    private Comparator<SearchNode> manhattanComparator() {
        return new ManhattanComparator();
    }

    private class ManhattanComparator implements Comparator<SearchNode> {
        // we need to use manhattan distance to compare the boards

        // what methods do we need for a comparator?
        public int compare(SearchNode o1, SearchNode o2) {
            // calculate o1 and o2 manhattan distances
            int d1 = o1.board.manhattan() + o1.moves;
            int d2 = o2.board.manhattan() + o2.moves;

            // do comparison and return relevant integer
            if (d1 > d2) return 1;
            else if (d2 > d1) return -1;
            return 0;
        }
    }

    public boolean isSolvable() {
        // to assess whether it's solvable we solve two versions of the
        // board in parallel, if the twin is resolved, our board is unsolvable
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        else {
            // create a stack (queue or stack is fine)
            SearchNode currentNode = minNode;
            while (currentNode != null) {
                solutionStack.push(currentNode.board);
                currentNode = currentNode.previous;
            }

            return solutionStack;
        }
    }

    public static void main(String[] args) {
        // int[][] tiles = { { 1, 2 }, { 0, 3 } };
        // int[][] tiles = { { 3, 0 }, { 2, 1 } };
        int[][] tiles = { { 1, 0, 2 }, { 7, 5, 4 }, { 8, 6, 3 } };
        // int[][] tiles = { { 2, 3, 5 }, { 1, 0, 4 }, { 7, 8, 6 } };
        Board b = new Board(tiles);
        Solver s = new Solver(b);

        System.out.print("Number of moves in solution: ");
        System.out.println(s.moves());

        System.out.println();
        System.out.println("Solution: ");
        for (Board b1 : s.solution()) {
            System.out.println(b1.toString());
        }
    }
}
