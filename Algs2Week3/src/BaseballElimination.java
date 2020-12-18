import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	private Map<String, Integer> teams;
	private String[] teamNames;
	private int numTeams;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	private boolean[] eliminated;
	private Bag<String>[] certificatesOfElimination;
	
	public BaseballElimination(String filename) {
		// create a baseball division from given filename in format specified below
		if (filename == null) throw new IllegalArgumentException();
		
		In in = new In(filename);
		teams = new HashMap<String, Integer>();
		numTeams = in.readInt();
		w = new int[numTeams];
		l = new int[numTeams];
		r = new int[numTeams];
		g = new int[numTeams][numTeams];
		eliminated = new boolean[numTeams];
		certificatesOfElimination = new Bag[numTeams];
		teamNames = new String[numTeams];
		
		int teamID = 0;
		in.readLine();
		while (!in.isEmpty()) {
//			String line = in.readLine();
//			String[] lineArray = line.split(" ");
//			teams.put(lineArray[0], teamID);
//			w[teamID] = Integer.parseInt(lineArray[1]);
//			l[teamID] = Integer.parseInt(lineArray[2]);
//			r[teamID] = Integer.parseInt(lineArray[3]);
//			for (int i = 0; i < numTeams; i++) {
//				g[teamID][i] = Integer.parseInt(lineArray[4 + i]);
//			}
//			teamID++;
			String teamName = in.readString();
			teams.put(teamName, teamID);
			teamNames[teamID] = teamName;
			w[teamID] = in.readInt();
			l[teamID] = in.readInt();
			r[teamID] = in.readInt();
			for (int i = 0; i < numTeams; i++) {
				g[teamID][i] = in.readInt();
			}
			eliminated[teamID] = false;
			certificatesOfElimination[teamID] = new Bag<String>();
			teamID++;
		}
		determineEliminations();
	}
	
	public int numberOfTeams() {
		// number of teams
		return numTeams;
	}
	
	public Iterable<String> teams() {
		// all teams
		return teams.keySet();
	}
	
	public int wins(String team) {
		// number of wins for given team
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		
		return w[teams.get(team)];
	}
	
	public int losses(String team) {
		// number of losses for given team
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		
		return l[teams.get(team)];
	}
	
	public int remaining(String team) {
		// number of remaining games for given team
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		
		return r[teams.get(team)];
	}
	
	public int against(String team1, String team2) {
		// number of remaining games between team1 and team2
		if (!teams.containsKey(team1) || !teams.containsKey(team2)) throw new IllegalArgumentException();
		
		return g[teams.get(team1)][teams.get(team2)];
	}
	
	public boolean isEliminated(String team) {
		// is given team eliminated?
		if (!teams.containsKey(team)) throw new IllegalArgumentException();

		return eliminated[teams.get(team)];
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		// subset R of teams that eliminates given team; null if not eliminated
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		
		if (!isEliminated(team))
			return null;

		return certificatesOfElimination[teams.get(team)];
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
	
	private void testInputBuilder() {
		StdOut.println(numberOfTeams());
		for (String team : teams()) {
			StdOut.print(team + " ");
			StdOut.print(wins(team) + " ");
			StdOut.print(losses(team) + " ");
			StdOut.print(remaining(team) + " ");
			for (String otherTeam : teams()) {
				StdOut.print(against(team, otherTeam) + " ");
			}
			StdOut.print(isEliminated(team) + " ");
			StdOut.print("R = { ");
			if (isEliminated(team)) { 
				for (String s : certificateOfElimination(team)) {
					StdOut.print(s + ", ");
				}
			}
			StdOut.print("} ");
			StdOut.print("\n");
		}
		StdOut.println();
	}
	
	private void determineEliminations() {
		if (numTeams == 1) 
			return;
		
	    int combinations = numberOfCombinations(0);

		for (int teamID = 0; teamID < numTeams; teamID++) {
			determineTrivialElimination(teamID);
			
			if (!eliminated[teamID]) {
				determineNontrivialElimination(teamID, combinations);
			}
		}
	}
	
	private void determineTrivialElimination(int teamID) {
		int potentialWins = w[teamID] + r[teamID];
		
		for (int i = 0; i < numTeams; i++) {
			if (i == teamID) 
				continue;
			
			if (w[i] > potentialWins) {
				eliminated[teamID] = true;
				certificatesOfElimination[teamID].add(teamNames[i]);
				return;
			}
		}
	}
	
	private void determineNontrivialElimination(int teamID, int combinations) {
		int s = numTeams;
		int t = numTeams+1;
		
		int V = 1 + combinations + (numTeams) + 1;
		
		FlowNetwork network = new FlowNetwork(V);
		
		for (int i = 0; i < numTeams; i++) {
			if (i == teamID)
				continue;
			
			network.addEdge(new FlowEdge(i, t, w[teamID] + r[teamID] - w[i]));
		}
		int totalFlow = 0;
		int gamesStart = t+1;
		int currentVertex = gamesStart;
		for (int i = 0; i < numTeams-1; i++) {
			if (i == teamID)
				continue;
			
			for (int j = i+1; j < numTeams; j++) {
				if (j == teamID)
					continue;
				
				totalFlow += g[i][j];
				network.addEdge(new FlowEdge(s, currentVertex, g[i][j]));
				network.addEdge(new FlowEdge(currentVertex, i, Integer.MAX_VALUE));
				network.addEdge(new FlowEdge(currentVertex, j, Integer.MAX_VALUE));
				currentVertex++;
			}
		}
		
		FordFulkerson ff = new FordFulkerson(network, s, t);
		if (ff.value() < totalFlow) {
		    eliminated[teamID] = true;
		    
		    for (int i = 0; i < numTeams; i++) {
	            if (i == teamID)
	                continue;
	            
	            if (ff.inCut(i)) {
	                certificatesOfElimination[teamID].add(teamNames[i]);
	            }
	        }
		}		
	}
	
	
	private int numberOfCombinations(int teamID) {
	    int combinations = 0;
	    for (int i = 0; i < numTeams-1; i++) {
            if (i == teamID)
                continue;
            
            for (int j = i+1; j < numTeams; j++) {
                if (j == teamID)
                    continue;
                
                combinations++;
            }
        }
	    
	    return combinations;
	}
}
