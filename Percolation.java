import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// TODO: we need to solve backwash

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF gridUnion;

    private int openSiteCount = 0;
    private int topVirtualSite;
    private int bottomVirtualSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // confirm n is greater than 0
        if (n <= 0) {
            throw new IllegalArgumentException("n must be a positive integer (n >= 0");
        } else {
            // create grid for marking open sites
            grid = new boolean[n][n];

            // record virtual site indexes
            topVirtualSite = 0;
            bottomVirtualSite = (n * n) + 1;

            // grid union is int[] one dimensional
            // we add 2 sites (one at the start and the end) which are our virtual sites
            //      this changes our indexing formula later on
            gridUnion = new WeightedQuickUnionUF((n * n) + 2);

            // O(n^2) approach to creating grid
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    grid[i][j] = false; // mark all sites closed with 0
                }
            }
        }
    }

    private int[][] getNeighbours(int row, int col) {
        return new int[][] {
                {row -1, col},
                {row, col + 1},
                {row + 1, col},
                {row, col - 1}
        };
    }

    private int getIndex(int row, int col) {
        // formula for index: idx = 1 + (row * N) + col
        //  NOTE: we need to add 1 since there is a virtual site at 0
        return 1 + ((row - 1) * grid.length) + (col - 1);
    }

    private boolean isValid(int loc) {
        return (0 < loc && loc <= grid.length);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValid(row) || !isValid(col)) {
            throw new IllegalArgumentException("Values for row and col must be: 1 < row|col <= n");
        } else if (!isOpen(row, col)) {
            // if the site isn't already open, open it
            grid[row - 1][col - 1] = true;
            openSiteCount += 1;

            // we also need to connect this site to any open sites next to it
            // we can define our site to connect
            int p = getIndex(row, col);

            // we need to connect to our virtual sites
            if (row == 1) gridUnion.union(p, topVirtualSite);

            // we can't use 'else if' because when n=1 we connect to top and bottom virtual sites
            if (row == grid.length) gridUnion.union(p, bottomVirtualSite);

            // create array of neighbouring sites
            // TODO: the getNeighbours should return indexes directly
            int[][] localSites = getNeighbours(row, col);

            // iterate through neighbouring sites and connect the open ones
            for (int[] localSite : localSites) {
                // we need to check if the localSite is valid
                if (isValid(localSite[0]) && isValid(localSite[1])) {
                    // if neighbour is open, we should run union
                    if (isOpen(localSite[0], localSite[1])) {
                        // our p and q come map to our 2 dimensional array
                        int q = getIndex(localSite[0], localSite[1]);
                        gridUnion.union(p, q);
                    }
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row) || !isValid(col)) {
            throw new IllegalArgumentException("Values for row and col must be: 1 < row|col <= n");
        } else return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row) || !isValid(col)) {
            throw new IllegalArgumentException("Values for row and col must be: 1 < row|col <= n");
        } else {
            // if a site shares the same parent the top Virtual site, it's connected (and full)
            // check whether the site connects to our top virtual site
            return gridUnion.find(0) == gridUnion.find(getIndex(row, col));
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // To use the virtual sites:
        //  We create two additional sites in our gridUnion,
        //      one at the start (0) and one at the and (n^2 + 1)
        //  We imagine these sites are always open, so when we open a site
        //      in row = 0 or row = n, we connect to our virtual sites.
        //  This does reduce the amount of information we have though,
        //      we no longer see the distinct holes that are connected,
        //      everything connected to the top becomes one group.
        //  But when we are trying to answer: does it percolate?
        //      we don't care about the lost information
        //  Visually this is:

        //    [0]     open(0, 1)              open(1, 1)              open(2, 1)
        // [1, 2, 3] ------------> [1, 0, 3] ------------> [1, 0, 3] ------------> [1, 0, 3]
        // [4, 5, 6] ------------> [4, 5, 6] ------------> [4, 0, 6] ------------> [4, 0, 6]
        // [7, 8, 9] ------------> [7, 8, 9] ------------> [7, 8, 9] ------------> [7, 0, 9]
        //    [10]                    [10]                    [10]                    [0]

        // check if the bottom virtual site is full (at row = N)
        // return isFull(grid.length, 1);
        return gridUnion.find(topVirtualSite) == gridUnion.find(bottomVirtualSite);
    }

    // test client (optional)
    public static void main(String[] args) {
        // instantiate percolation class
        Percolation myPerc = new Percolation(3);
        //    [0]
        // [1, 2, 3]
        // [4, 5, 6]
        // [7, 8, 9]
        //    [10]

        // open some pores
        myPerc.open(1, 2);
        myPerc.open(2, 2);
        myPerc.open(2, 1);
        myPerc.open(3, 1);

        // test some functions and print
        System.out.println("There are " + myPerc.numberOfOpenSites() + " open sites");
        System.out.println("Site 2, 2 is full: " + myPerc.isFull(2, 2));
        System.out.println("Percolates: " + myPerc.percolates());
    }

}
