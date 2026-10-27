/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int cells = w.rows() * w.cols();
        int cols = w.cols();

        int[][] graph = Graphs.open(w, broken);

        boolean[] visited = new boolean[cells];

        int[] parent = new int[cells];

        java.util.Arrays.fill(parent, -1);

        int visitedCount = 0;
        int deadEnds = 0;

        for (int i = 0; i < cells; i++) {
            if (graph[i].length == 1) {
                deadEnds++;
            }
        }

        java.util.ArrayDeque<Integer> stack =
                new java.util.ArrayDeque<>();

        stack.push(0);

        visited[0] = true;
        visitedCount++;
        int[] nextDirection = new int[cells];
        int exit = cells - 1;

        while (!stack.isEmpty() && !visited[exit]) {
            int current = stack.peek();

            if (nextDirection[current] == 4) {
                stack.pop();
                continue;
            }

            int direction = nextDirection[current]++;
            int next = -1;

            if (direction == 0 && current >= cols) {
                next = current - cols;
            } else if (direction == 1 && current % cols < cols - 1) {
                next = current + 1;
            } else if (direction == 2 && current + cols < cells) {
                next = current + cols;
            } else if (direction == 3 && current % cols > 0) {
                next = current - 1;
            }

            if (next == -1 || visited[next]) {
                continue;
            }

            boolean open = false;

            for (int neighbor : graph[current]) {
                if (neighbor == next) {
                    open = true;
                    break;
                }
            }

            if (!open) {
                continue;
            }

            visited[next] = true;
            visitedCount++;
            parent[next] = current;
            stack.push(next);
        }
        if (!visited[exit]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }
        int length = 0;

        for (int v = exit; v != -1; v = parent[v]) {
            length++;
        }

        int[] path = new int[length];

        int index = length - 1;

        for (int v = exit; v != -1; v = parent[v]) {
            path[index] = v;
            index--;
        }


        return new Result(path, visitedCount, deadEnds);
    }
}
