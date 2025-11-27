import java.util.*;

/**
 * Represents a single state in the Tower of Hanoi puzzle for A* search.
 */
class StateAStar {
    List<Integer> pegA, pegB, pegC;
    StateAStar parent;
    String move;
    int g;   // cost so far
    int h;   // heuristic estimate

    StateAStar(List<Integer> a, List<Integer> b, List<Integer> c, StateAStar par, String m, int gCost) {
        this.pegA = new ArrayList<>(a);
        this.pegB = new ArrayList<>(b);
        this.pegC = new ArrayList<>(c);
        this.parent = par;
        this.move = m;
        this.g = gCost;
        this.h = heuristic();
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

    int heuristic() {
        return pegA.size() + pegB.size();  // number of disks not on peg C
    }

    int f() {
        return g + h;
    }
}

public class TowerOfHanoiAStar {

    static List<StateAStar> getSuccessors(StateAStar s) {
        List<StateAStar> successors = new ArrayList<>();
        char[] pegNames = {'A', 'B', 'C'};

        @SuppressWarnings("unchecked")
        List<Integer>[] pegs = new List[]{s.pegA, s.pegB, s.pegC};

        for (int from = 0; from < 3; from++) {
            if (pegs[from].isEmpty()) continue;

            int disk = pegs[from].get(pegs[from].size() - 1);

            for (int to = 0; to < 3; to++) {
                if (from == to) continue;

                if (pegs[to].isEmpty() || disk < pegs[to].get(pegs[to].size() - 1)) {
                    List<Integer> newA = new ArrayList<>(s.pegA);
                    List<Integer> newB = new ArrayList<>(s.pegB);
                    List<Integer> newC = new ArrayList<>(s.pegC);

                    @SuppressWarnings("unchecked")
                    List<Integer>[] newPegs = new List[]{newA, newB, newC};

                    newPegs[from].remove(newPegs[from].size() - 1);
                    newPegs[to].add(disk);

                    String move = "Move disk " + disk + " from " + pegNames[from] + " to " + pegNames[to];

                    successors.add(new StateAStar(newA, newB, newC, s, move, s.g + 1));
                }
            }
        }
        return successors;
    }

    public static void main(String[] args) {

        System.out.println("=== Tower of Hanoi (3 Disks) - A* Search ===\n");

        // -------- INITIAL STATE ----------
        List<Integer> A = Arrays.asList(3, 2, 1);
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        StateAStar start = new StateAStar(A, B, C, null, "Initial State", 0);

        System.out.println("--- Initial State ---");
        start.printState();
        System.out.println();

        PriorityQueue<StateAStar> open = new PriorityQueue<>(Comparator.comparingInt(StateAStar::f));
        Set<String> closed = new HashSet<>();

        open.add(start);

        while (!open.isEmpty()) {
            StateAStar current = open.poll();

            if (current.isGoal()) {

                // Reconstruct solution path
                List<StateAStar> path = new ArrayList<>();
                while (current != null) {
                    path.add(0, current);
                    current = current.parent;
                }

                System.out.println("--- Solution Path (A*) ---\n");

                for (int i = 1; i < path.size(); i++) {
                    System.out.println("Step " + i + ": " + path.get(i).move);
                    path.get(i).printState();
                    System.out.println();  // blank line after every step
                }

                // Print GOAL STATE at the end
                System.out.println("--- GOAL STATE ---");
                path.get(path.size() - 1).printState();

                System.out.println("\nTotal Moves = " + (path.size() - 1));
                return;
            }

            closed.add(current.getKey());

            for (StateAStar next : getSuccessors(current)) {
                if (!closed.contains(next.getKey())) {
                    open.add(next);
                }
            }
        }
    }
}
