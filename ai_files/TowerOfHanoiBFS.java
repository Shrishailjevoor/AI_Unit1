import java.util.*;

/**
 * Represents a single state (configuration) of the Tower of Hanoi puzzle.
 */
class StateBFS {
    // Lists represent the pegs, with the top element (disk) at the end of the list.
    List<Integer> pegA, pegB, pegC;
    StateBFS parent;
    String move;

    StateBFS(List<Integer> a, List<Integer> b, List<Integer> c, StateBFS par, String m) {
        // Deep copy the lists to ensure the state is immutable after creation
        pegA = new  ArrayList<>(a);
        pegB = new  ArrayList<>(b);
        pegC = new  ArrayList<>(c);
        parent = par;
        move = m;
    }

    /**
     * Checks if the current state is the goal state: all 3 disks are on Peg C.
     */
    boolean isGoal() {
        return pegA.isEmpty() && pegB.isEmpty() && pegC.size() == 3;
    }

    /**
     * Generates a unique string key for the current state configuration.
     */
    String getKey() {
        return pegA.toString() + pegB.toString() + pegC.toString();
    }

    /**
     * Prints the current configuration of the pegs.
     */
    void printState() {
        System.out.println("| Peg A: " + pegA + " | Peg B: " + pegB + " | Peg C: " + pegC + " |");
    }
}

/**
 * Solves the Tower of Hanoi puzzle using BFS and tracks the state at each step.
 */
public class TowerOfHanoiBFS {

    /**
     * Generates all valid successor states from a given current state.
     * (Logic remains the same as standard BFS implementation)
     */
    static List<StateBFS> getSuccessors(StateBFS s) {
        List<StateBFS> succ = new ArrayList<>();
        char[] names = {'A', 'B', 'C'};
        @SuppressWarnings("unchecked")
        List<Integer>[] pegs = new List[]{s.pegA, s.pegB, s.pegC};

        for(int from = 0; from < 3; from++) {
            if(!pegs[from].isEmpty()) {
                int disk = pegs[from].get(pegs[from].size() - 1);

                for(int to = 0; to < 3; to++) {
                    if(from != to) {
                        if(pegs[to].isEmpty() || disk < pegs[to].get(pegs[to].size() - 1)) {

                            List<Integer> newA = new ArrayList<>(s.pegA);
                            List<Integer> newB = new ArrayList<>(s.pegB);
                            List<Integer> newC = new ArrayList<>(s.pegC);

                            @SuppressWarnings("unchecked")
                            List<Integer>[] newPegs = new List[]{newA, newB, newC};

                            newPegs[from].remove(newPegs[from].size() - 1);
                            newPegs[to].add(disk);

                            String m = "Move disk " + disk + " from " + names[from] + " to " + names[to];

                            succ.add(new StateBFS(newA, newB, newC, s, m));
                        }
                    }
                }
            }
        }
        return succ;
    }

    public static void main(String[] args) {
        System.out.println("=== Tower of Hanoi (3 Disks) - BFS Step Tracker ===\n");

        // Initial State: Disks [3, 2, 1] on Peg A
        List<Integer> a = Arrays.asList(3, 2, 1);
        List<Integer> b = new ArrayList<>();
        List<Integer> c = new ArrayList<>();
        StateBFS start = new StateBFS(a, b, c, null, "Initial State");

        System.out.println("--- Initial State ---");
        start.printState();
        System.out.println();

        Queue<StateBFS> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(start);
        visited.add(start.getKey());

        while(!queue.isEmpty()) {
            StateBFS curr = queue.poll();

            if(curr.isGoal()) {
                // Goal found! Reconstruct and print the path and states.
                List<StateBFS> path = new ArrayList<>();
                while(curr != null) {
                    path.add(0, curr);
                    curr = curr.parent;
                }

                System.out.println("--- Path to Goal Found ---");

                // Start printing from the first move (i=1)
                for(int i = 1; i < path.size(); i++) {
                    StateBFS step = path.get(i);
                    System.out.println("\n Step " + i + ": " + step.move);
                    step.printState();
                }

                System.out.println("\n GOAL STATE REACHED!");
                path.get(path.size() - 1).printState();
                System.out.println("\nTotal minimum moves: " + (path.size()-1));
                return;
            }

            // Explore successors
            List<StateBFS> successors = getSuccessors(curr);
            for(StateBFS next : successors) {
                if(!visited.contains(next.getKey())) {
                    visited.add(next.getKey());
                    queue.add(next);
                }
            }
        }
    }
}