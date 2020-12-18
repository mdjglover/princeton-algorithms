import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver 
{
    private int m_minMoves;
    private MinPQ<Node> pqMain;
    private MinPQ<Node> pqTwin;
    private Stack<Board> m_solution;
    private boolean m_goalReached;
    private boolean m_solvable;
    
    private class Node implements Comparable<Node>
    {
        private Board currentBoard;
        private Node prevNode;
        private int moves;
        private int priority;
        
        public Node(Board current, Node prevNode) 
        {
            // TODO Auto-generated constructor stub
            this.currentBoard = current;
            this.prevNode = prevNode;
            if (prevNode != null)
                moves = prevNode.moves + 1;
            else
                moves = 0;
            
            priority = moves + current.manhattan();
        }
        
        @Override
        public int compareTo(Node x) 
        {
            // TODO Auto-generated method stub
            if (this.priority < x.priority)
                return -1;
            if (this.priority > x.priority)
                return +1;
            else
                return 0;
        }
        
    }
    
    public Solver(Board initial)
    {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) throw new IllegalArgumentException();
        
        m_solvable = false;
        m_goalReached = false;
        m_solution = new Stack<Board>();
        pqMain = new MinPQ<Node>();
        pqTwin = new MinPQ<Node>();
        
        pqMain.insert(new Node(initial, null));
        pqTwin.insert(new Node(initial.twin(), null));
        
        while (!m_goalReached)
        {
            Node mainNode = pqMain.delMin();
            if (mainNode.currentBoard.isGoal())
            {
                m_goalReached = true;
                m_solvable = true;
                m_minMoves = mainNode.moves;
                buildSolutionStack(mainNode);
                break;
            }
            
            Node twinNode = pqTwin.delMin();
            if (twinNode.currentBoard.isGoal())
            {
                m_goalReached = true;
                m_solvable = false;
                m_solution = null;
                break;
            }
            
            for (Board board : mainNode.currentBoard.neighbors())
            {
                if (mainNode.prevNode == null || !board.equals(mainNode.prevNode.currentBoard))
                    pqMain.insert(new Node(board, mainNode));
            }
            
            for (Board board : twinNode.currentBoard.neighbors())
            {
                if (twinNode.prevNode == null || !board.equals(twinNode.prevNode.currentBoard))
                    pqTwin.insert(new Node(board, twinNode));
            }
        }
    }
    
    private void buildSolutionStack(Node node)
    {
        while (node != null)
        {
            m_solution.push(node.currentBoard);
            node = node.prevNode;
        }
    }
    
    public boolean isSolvable()
    {
        // is the initial board solvable?
        return m_solvable;
    }
    
    public int moves()
    {
        // min number of moves to solve initial board; -1 if unsolvable
        if (isSolvable())
            return m_minMoves;
        
        else return -1;
    }
    
    public Iterable<Board> solution()
    {
        // sequence of boards in a shortest solution; null if unsolvable
        return m_solution;
    }
    
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}
