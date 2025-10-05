import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] p;
    private int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be >= 0");
        }

        // declare variable p as array length(trials) - one for each
        p = new double[trials];
        t = trials;

        // we need to have a test for every trial
        for (int i = 0; i < trials; i++) {
            // initialise with all sites blocked
            Percolation myPerc = new Percolation(n);

            // use a while loop to run until percolated
            while (!myPerc.percolates()) {
                // open a random site
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                myPerc.open(row, col);
            }

            // once percolated we can take the number of open sites to get p
            p[i] = (double) myPerc.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(p);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(p);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * Math.sqrt(stddev())) / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * Math.sqrt(stddev())) / Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        // cast args to string
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        // run percolation trials
        PercolationStats percStats = new PercolationStats(n, trials);

        // print some results
        System.out.print("mean \t\t\t\t\t= ");
        System.out.println(percStats.mean());
        System.out.print("stddev \t\t\t\t\t= ");
        System.out.println(percStats.stddev());
        System.out.print("95% confidence interval = [");
        System.out.print(percStats.confidenceLo());
        System.out.print(", ");
        System.out.print(percStats.confidenceHi());
        System.out.println("]");
    }
}
