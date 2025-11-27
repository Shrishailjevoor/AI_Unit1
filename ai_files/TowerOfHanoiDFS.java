import java.util.*;

/**
 * Represents a single state (configuration) of the Tower of Hanoi puzzle.
 */
class StateDFS {
    List<Integer> pegA, pegB, pegC;
    StateDFS parent;
    String move;

    StateDFS(List<Integer> a, List<Integer> b, List<Integer> c, StateDFS par, String m) {
        pegA = new ArrayList<>(a);
        pegB = new ArrayList<>(b);
        pegC = new ArrayList<>(c);
        parent = par;
        move = m;
    }

    boolean isGoal() {
        return pegA.isEmpty() && pegB.isEmpty() && pegC.size() == 3;
    }

    String getKey() {
        return pegA.toString() + pegB.toString() + pegC.toString();
    }

    void printState() {
        System.out.println("| Peg A: " + pegA + " | Peg B: " + pegB + " | Peg C: " + pegC + " |");
    }
}

/**
 * Solves the Tower of Hanoi puzzle using DFS (Depth-First Search).
 */
public class TowerOfHanoiDFS {

    /** Generate all valid successor states (same logic as BFS version). */
    static List<StateDFS> getSuccessors(StateDFS s) {
        List<StateDFS> succ = new ArrayList<>();
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

                            succ.add(new StateDFS(newA, newB, newC, s, m));
                        }
                    }
                }
            }
        }
        return succ;
    }

    public static void main(String[] args) {

        System.out.println("=== Tower of Hanoi (3 Disks) - DFS Step Tracker ===\n");

        List<Integer> a = Arrays.asList(3, 2, 1);
        List<Integer> b = new ArrayList<>();
        List<Integer> c = new ArrayList<>();

        StateDFS start = new StateDFS(a, b, c, null, "Initial State");

        System.out.println("--- Initial State ---");
        start.printState();
        System.out.println();

        Stack<StateDFS> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        stack.push(start);
        visited.add(start.getKey());

        while(!stack.isEmpty()) {
            StateDFS curr = stack.pop();

            if(curr.isGoal()) {

                List<StateDFS> path = new ArrayList<>();
                while(curr != null) {
                    path.add(0, curr);
                    curr = curr.parent;
                }

                System.out.println("--- Path to Goal Found (DFS) ---");

                for(int i = 1; i < path.size(); i++) {
                    StateDFS step = path.get(i);
                    System.out.println("\n Step " + i + ": " + step.move);
                    step.printState();
                }

                System.out.println("\n GOAL STATE REACHED (DFS)!");
                path.get(path.size() - 1).printState();
                System.out.println("\nTotal moves found by DFS: " + (path.size()-1));
                return;
            }

            List<StateDFS> successors = getSuccessors(curr);

            for(StateDFS next : successors) {
                if(!visited.contains(next.getKey())) {
                    visited.add(next.getKey());
                    stack.push(next);
                }
            }
        }
    }
}
