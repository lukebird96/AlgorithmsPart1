import edu.princeton.cs.algs4.Queue;

public class Board {
    private int dimension;
    private int[][] board;

    public Board(int[][] tiles) {
        // initialise variables
        dimension = tiles.length;
        board = new int[dimension][dimension];

        // iterate through
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                board[row][col] = tiles[row][col];
            }
        }
    }

    private int[][] getTiles() {
        return board;
    }

    //
    public String toString() {
        StringBuilder s = new StringBuilder(Integer.toString(dimension));

        // we can't use Arrays.toString() because it leaves brackets
        for (int i = 0; i < dimension; i++) {
            s.append("\n");

            for (int j = 0; j < dimension; j++) {
                s.append(" ").append(Integer.toString(board[i][j]));
            }
        }
        return s.toString();
    }

    //
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDistance = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int goalTile = (dimension * i) + j + 1;
                if (board[i][j] != goalTile && board[i][j] != 0) {
                    hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    // sum of manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;

        // sum of vertical and horizontal indexes
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int boardTile = board[i][j];

                if (boardTile != 0) {
                    // floor division gives the row (always)
                    int goalRow = (boardTile - 1) / dimension;
                    // we can use modulo to get col
                    int goalCol = (boardTile - 1) % dimension;

                    int dRow;
                    int dCol;

                    // we need to take absolute distance
                    if (goalRow > i) {
                        dRow = (goalRow - i);
                    }
                    else {
                        dRow = (i - goalRow);
                    }

                    // we need to take absolute distance
                    if (goalCol > j) {
                        dCol = (goalCol - j);
                    }
                    else {
                        dCol = (j - goalCol);
                    }

                    // sum to get distance
                    int d = dRow + dCol;

                    // add to board level distance
                    manhattanDistance += d;
                }
            }
        }

        return manhattanDistance;
    }

    // is this board the goal board
    public boolean isGoal() {
        // don't need to check dimension
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int goalTile = (dimension * i) + j + 1;
                if (board[i][j] != goalTile && board[i][j] != 0) {
                    return false;
                }
            }
        }

        // we could construct another board object with the goalBoard tiles
        //  and pass that to equals(). It would be slower but maybe more correct

        return true;
    }

    // does this board equal y (another board)
    public boolean equals(Object y) {
        if (y == null) return false;
        else if (y.getClass() != this.getClass()) return false;

        // we have to cast to board
        final Board other = (Board) y;

        // check if the board is the same size
        if (other.dimension() != dimension) return false;
        else {
            int[][] otherTiles = other.getTiles();
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (board[i][j] != otherTiles[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // create empty queue
        Queue<Board> boardQueue = new Queue<Board>();

        // add neighbors to queue
        // start by finding the 0 tile
        int row = 0;
        int col = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // we can initialise the spaceTile location now
        int[] spaceTile = { row, col };

        // set row-wise swaps for neighbors
        if (row == 0) {
            // (0, 0) -> swap (1, 0)
            int[] swapRight = new int[] { row + 1, col };
            boardQueue.enqueue(exch(spaceTile, swapRight));
        }
        else if (row == dimension - 1) {
            // (N-1, N-1) -> swap (N-2, N-1)
            int[] swapLeft = new int[] { row - 1, col };
            boardQueue.enqueue(exch(spaceTile, swapLeft));
        }
        else {
            // in the middle
            int[] swapRight = new int[] { row + 1, col };
            int[] swapLeft = new int[] { row - 1, col };
            boardQueue.enqueue(exch(spaceTile, swapRight));
            boardQueue.enqueue(exch(spaceTile, swapLeft));
        }

        // set col-wise swaps for neighbors
        if (col == 0) {
            // (0, 0) -> swap (0, 1)
            int[] swapUp = new int[] { row, col + 1 };
            boardQueue.enqueue(exch(spaceTile, swapUp));
        }
        else if (col == dimension - 1) {
            // (N-1, N-1) -> swap (N-1, N-2)
            int[] swapDown = new int[] { row, col - 1 };
            boardQueue.enqueue(exch(spaceTile, swapDown));
        }
        else {
            int[] swapUp = new int[] { row, col + 1 };
            int[] swapDown = new int[] { row, col - 1 };
            boardQueue.enqueue(exch(spaceTile, swapUp));
            boardQueue.enqueue(exch(spaceTile, swapDown));
        }

        return boardQueue;
    }

    // exchange position p1 with position p2
    private Board exch(int[] p1, int[] p2) {
        if (p2 == null) {
            return null;
        }

        int[][] newTiles = new int[dimension][dimension];

        // create a copy of this board
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                newTiles[i][j] = board[i][j];
            }
        }

        // get row and col from the positions
        int rowStart = p1[0];
        int colStart = p1[1];
        int rowTarget = p2[0];
        int colTarget = p2[1];

        // we need to implement an exchange method?
        newTiles[rowStart][colStart] = board[rowTarget][colTarget];
        newTiles[rowTarget][colTarget] = board[rowStart][colStart];

        return new Board(newTiles);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // I am assuming this is for unsolvable boards
        // which require any swap to work
        int[] p1 = { 0, 0 };
        int[] p2 = { 0, 1 };

        if (board[0][0] == 0) {
            p1 = new int[] { 1, 1 };
        }
        else if (board[0][1] == 0) {
            p2 = new int[] { 1, 0 };
        }

        return exch(p1, p2);
    }

    public static void main(String[] args) {
        // int[][] tiles = { { 1, 2 }, { 0, 3 } };
        // int[][] tiles = { { 0, 3 }, { 2, 1 } };
        // int[][] tiles = { { 2, 0 }, { 3, 1 } };
        int[][] tiles = { { 1, 2 }, { 3, 0 } };
        Board b = new Board(tiles);
        System.out.println("Board: ");
        System.out.println(b.toString());

        System.out.print("Is Goal Board? ");
        System.out.println(b.isGoal());

        System.out.print("Hamming Score: ");
        System.out.println(b.hamming());

        System.out.print("Manhattan Score: ");
        System.out.println(b.manhattan());

        System.out.println("Checking neighbor boards: ");
        for (Board b1 : b.neighbors()) {
            System.out.println(b1.toString());
        }

        Board b1 = new Board(tiles);

        System.out.println(b.equals(b1));

    }
}
