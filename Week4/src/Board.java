import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board 
{
    private int m_dimension;
    private int m_hamming;
    private int m_manhattan;
    private int[] m_board;
    private int m_blankPosition;
    
    public Board(int[][] blocks)
    {
        // construct a board from an n-by-n array of blocks                                      
        // (where blocks[i][j] = block in row i, column j)
        m_dimension = blocks.length;
        m_board = new int[m_dimension * m_dimension];
        
        int index = 0;
        for (int i = 0; i < m_dimension; i++)
        {
            for (int j = 0; j < m_dimension; j++)
            {
                if (blocks[i][j] == 0)
                    m_blankPosition = index;
                
                m_board[index++] = blocks[i][j];
            }
        }
        
        m_hamming = calculateHamming(m_board);
        m_manhattan = calculateManhattan(m_board);
    }
    
    public int dimension()
    {
        // board dimension n
        return m_dimension;
    }
    
    private int calculateHamming(int[] board)
    {
        int hamming = 0;
        for (int i = 0; i < board.length; i++)
        {
            if (board[i] == 0) 
                continue;
            
            if (board[i] != i+1)
            {
                hamming += 1;
            }
        }
        
        return hamming;
    }
    
    public int hamming()
    {
        // number of blocks out of place
        return m_hamming;
    }
    
    private int calculateManhattan(int[] board)
    {
        int manhattan = 0;
        
        for (int i = 0; i < board.length; i++)
        {
            if (board[i] == 0)
                continue;
            
            if (board[i] != i+1)
            {
                int currentX = (i+1) % m_dimension;
                int currentY = (i+1) / m_dimension;
                
                int targetX = board[i] % m_dimension;
                int targetY = board[i] / m_dimension;
                
                manhattan += Math.abs(currentX - targetX) + Math.abs(currentY - targetY);
            }
        }
        
        return manhattan;
    }
    
    public int manhattan()
    {
        // sum of Manhattan distances between blocks and goal
        return m_manhattan;
    }
    
    public boolean isGoal()
    {
        // is this board the goal board?
        for (int i = 0; i < m_board.length - 1; i++)
        {
            if (m_board[i] != i+1)
                return false;
        }
        return true;
    }
    
    public Board twin()
    {
        // a board that is obtained by exchanging any pair of blocks
        int[][] twin = findTwin(convertToBlocks(m_board));
        
        return new Board(twin);
    }
    
    private int[][] findTwin(int[][] blocks)
    {                
        for (int i = 0; i < m_dimension; i++)
        {
            if (blocks[0][i] != 0)
            {
                if (i+1 < m_dimension && blocks[0][i+1] != 0)
                {
                    int temp = blocks[0][i];
                    blocks[0][i] = blocks[0][i+1];
                    blocks[0][i+1] = temp;
                }
                else
                {
                    int temp = blocks[0][i];
                    blocks[0][i] = blocks[1][i];
                    blocks[1][i] = temp;
                }
                
                break;
            }
            
        }
        
        return blocks;
    }
    
    private int[][] convertToBlocks(int[] board)
    {
        int[][] blocks = new int[m_dimension][m_dimension];
        int col = 0;
        int row = 0;
        
        for (int i = 0; i < board.length; i++)
        {
            col = i % m_dimension;
            row = i / m_dimension;
            
            blocks[row][col] = board[i];
        }
        
        return blocks;
    }
    
    public boolean equals(Object y)
    {
        // does this board equal y?
        if (y == null)
            return false;
        
        if (this.getClass() != y.getClass())
            return false;
        
        Board that = (Board) y;
        if (this.m_dimension != that.m_dimension)
            return false;
        
        for (int i = 0; i < m_board.length; i++)
        {
            if (this.m_board[i] != that.m_board[i])
                return false;
        }
        
        return true;
    }
    
    private enum TileSwapDirection
    {
        UP,
        RIGHT,
        LEFT,
        DOWN
    }
    
    private boolean canMoveTile(TileSwapDirection direction)
    {
        int blankX = m_blankPosition % m_dimension;
        int blankY = m_blankPosition / m_dimension;
        
        if (direction == TileSwapDirection.UP)
        {
            return blankY > 0;
        }
        
        else if (direction == TileSwapDirection.RIGHT)
        {
            return blankX < m_dimension - 1;
        }
        
        else if (direction == TileSwapDirection.DOWN)
        {
            return blankY < m_dimension - 1;
        }
        
        else
        {
            return blankX > 0;
        }
    }
    private Board getNeighbour(TileSwapDirection direction)
    {
        int[][] blocks = convertToBlocks(m_board);
        
        int blankX = m_blankPosition % m_dimension;
        int blankY = m_blankPosition / m_dimension;
        
        if (direction == TileSwapDirection.UP)
        {
            exch(blocks, blankX, blankY, blankX, blankY-1);
        }
        else if (direction == TileSwapDirection.RIGHT)
        {
            exch(blocks, blankX, blankY, blankX+1, blankY);
        }
        else if (direction == TileSwapDirection.DOWN)
        {
            exch(blocks, blankX, blankY, blankX, blankY+1);
        }
        else
        {
            exch(blocks, blankX, blankY, blankX-1, blankY);
        }
        
        return new Board(blocks);
    }
    
    public Iterable<Board> neighbors()
    {
        // all neighboring boards
        Queue<Board> neighbours = new Queue<>();
        
        if (canMoveTile(TileSwapDirection.UP))
            neighbours.enqueue(getNeighbour(TileSwapDirection.UP));
        
        if (canMoveTile(TileSwapDirection.RIGHT))
            neighbours.enqueue(getNeighbour(TileSwapDirection.RIGHT));

        if (canMoveTile(TileSwapDirection.DOWN))
            neighbours.enqueue(getNeighbour(TileSwapDirection.DOWN));

        if (canMoveTile(TileSwapDirection.LEFT))
            neighbours.enqueue(getNeighbour(TileSwapDirection.LEFT));

        return neighbours;
    }
    
    private void exch(int[][] blocks, int ax, int ay, int bx, int by)
    {
        int temp = blocks[ay][ax];
        blocks[ay][ax] = blocks[by][bx];
        blocks[by][bx] = temp;
    }
    
    public String toString()
    {
        // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(m_dimension + "\n");
        int endline = m_dimension-1;
        
        for (int i = 0; i < m_board.length; i++) {
            
            s.append(String.format("%2d ", m_board[i]));
            
            if (i % m_dimension == endline)
                s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args)
    {
        // unit tests (not graded)
        int[][] blocks = {{0, 1, 2},
                          {3, 4, 5},
                          {6, 7, 8}};
        
        Board board = new Board(blocks);
        StdOut.println(board.toString());
        
        StdOut.println(board.isGoal());
        
        int[][] blocks2 = {{1, 2, 3},
                           {4, 5, 6},
                           {7, 8, 0}};
        
        Board board2 = new Board(blocks2);
        StdOut.println(board2.isGoal());
        
        Queue<Board> neighbours = (Queue<Board>) board.neighbors();
        for (Board b : neighbours)
        {
            StdOut.println(b.toString());
        }
        
        
    }
}
