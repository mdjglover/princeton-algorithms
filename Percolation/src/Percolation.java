import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation 
{
    private int n;
    private int N;
    private int top;
    private int bottom;
    private WeightedQuickUnionUF uf;

    private boolean[] sites;
    private int openSites;

    public Percolation(int n)
    {
        // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
    
        this.n = n;
        N = n*n+2;
    
        uf = new WeightedQuickUnionUF(N);
        sites = new boolean[N];
        top = 0;
        bottom = N-1;
        openSites = 0;
    
        for (int i = 0; i < N; ++i)
        {
            if (i == 0 || i == N-1)
            {
                sites[i] = true;
            }
            else {
                sites[i] = false;
            }
            
        }
    }
    
    public void open(int row, int col)
    {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    
        // open site (row, col) if it is not open already
        if (isOpen(row, col)) {
            return;
        }
    
        int index = getIndex(row, col);
        sites[index] = true;
        openSites += 1;
    
        // row up = index - n; row down = index + n; 
        // col next = index + 1; col before = index - 1
        if (row > 1 && isOpen(row-1, col)) {
            uf.union(index, index-n);
        }
    	
        if (row < n && isOpen(row+1, col)) {
            uf.union(index, index+n);
        }
    	
        if (col > 1 && isOpen(row, col-1)) {
            uf.union(index, index-1);
        }
    
        if (col < n && isOpen(row, col+1)) {
            uf.union(index, index+1);
        }
        
        if (row == 1) {
            uf.union(index, top);
        }
        
        if (row == n) {
            uf.union(index, bottom);
        }
    }
    
    public boolean isOpen(int row, int col)
    {
        // is site (row, col) open?
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    
        return sites[getIndex(row, col)];
    }
    
    public boolean isFull(int row, int col)
    {
        // is site (row, col) full?
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        
        return uf.connected(getIndex(row, col), top);
    }
    
    public int numberOfOpenSites()
    {
        // number of open sites
        return openSites;
    }
    
    public boolean percolates()
    {
        // does the system percolate?
        return uf.connected(top, bottom);
    }
    
    private int getIndex(final int row, final int col)
    {
        return (row-1) * n + col;
    }
    
    public static void main(final String[] args) 
    {
        // TODO Auto-generated method stub
    
    }

}
