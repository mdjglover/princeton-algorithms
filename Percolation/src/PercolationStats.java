import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int T;
    private int N;
    private double[] tResults;

    public PercolationStats(int n, int trials)
    {
       // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        T = trials;
        N = n*n;
        tResults = new double[T];

        for (int i = 0; i < T; ++i)
        {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates())
            {
                percolation.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
            }
            tResults[i] = (double)percolation.numberOfOpenSites() / N;
        }

    }
    
    public double mean()
    {
        // sample mean of percolation threshold
        return StdStats.mean(tResults);
    }
    
    public double stddev()
    {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(tResults);
    }
    
    public double confidenceLo()
    {
        // low  endpoint of 95% confidence interval
        return mean() - ((1.96 * stddev()) / Math.sqrt(T));
    }
    
    public double confidenceHi()
    {
        // high endpoint of 95% confidence interval
        return mean() + ((1.96 * stddev()) / Math.sqrt(T));
    }

    public static void main(String[] args) 
    {
        // TODO Auto-generated method stub
        if (args.length != 2)
            return;

        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
    
        PercolationStats pStats = new PercolationStats(n, T);
        System.out.println("mean \t\t\t = " + pStats.mean());
        System.out.println("stddev \t\t\t = " + pStats.stddev());
        System.out.println("95% confidence interval  = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + "]");
    }

}
