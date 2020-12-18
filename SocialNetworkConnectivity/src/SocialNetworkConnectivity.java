import edu.princeton.cs.algs4.StdIn

public class SocialNetworkConnectivity {
	
	private int[] parent;
	private int[] sz;
	private int N;
	private int maxConnectedMembers;
	
	public SocialNetworkConnectivity(int n)
	{
		N = n;
		parent = new int[N];
		sz = new int[N];
		maxConnectedMembers = 0;
		
		for (int i = 0; i < N; ++i)
		{
			parent[i] = i;
			sz[i] = 1;
		}
	}
	
	private int find(int p)
	{
		while (p != parent[p])
		{
			parent[p] = parent[parent[p]];
			p = parent[p];
		}
		return p;
	}
	
	private boolean union(int p, int q)
	{
		int pRoot = find(p);
		int qRoot = find(q);
		
		if (pRoot == qRoot)
			return false;
					
		
		if (sz[pRoot] <= sz[qRoot])
		{
			parent[pRoot] = parent[qRoot];
			sz[qRoot] += sz[pRoot];
			if (maxConnectedMembers < sz[qRoot])
			{
				maxConnectedMembers = sz[qRoot];
			}
		}
		else
		{
			parent[qRoot] = parent[pRoot];
			sz[pRoot] += sz[qRoot];
			if (maxConnectedMembers < sz[pRoot])
			{
				maxConnectedMembers = sz[pRoot];
			}
		}
		
		if (maxConnectedMembers == N)
			return true;
		else
			return false;
	}
	
	public boolean connected(int p, int q)
	{
		if (find(p) == find(q))
		{
			return true;
		}
		
		return false;
	}

	public static void main(String[] args) 
	{
		int n = StdIn.readInt();
        SocialNetworkConnectivity uf = new SocialNetworkConnectivity(n);
        boolean allConnected = false;
        int ts;
        while (!StdIn.isEmpty()) {
        	ts = StdIn.readInt();
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            allConnected = uf.union(p, q);
            if (allConnected)
            	break;
        }
	}

}
