/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int cells = w.rows() * w.cols();

        DisjointSet dsu = new DisjointSet(cells);

        int[] broken = new int[cells - 1];
        int count = 0;

        for (int k = 0; k < w.a().length; k++) {
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[count] = k;
                count++;
            }
        }
        return broken;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int cells = w.rows() * w.cols();

        DisjointSet dsu = new DisjointSet(cells);
        Integer[] order = new Integer[w.a().length];

        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        java.util.Arrays.sort(order,
                (a, b) -> Integer.compare(w.cost()[a], w.cost()[b]));

        int[] broken = new int[cells - 1];
        int count = 0;

        for (int k : order) {
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[count] = k;
                count++;
            }
        }

        return broken;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int cells = w.rows() * w.cols();

        boolean[] visited = new boolean[cells];

        long totalCost = 0;

        java.util.PriorityQueue<int[]> pq =
                new java.util.PriorityQueue<>(
                        (a, b) -> Integer.compare(a[0], b[0])
                );
        java.util.ArrayList<int[]>[] graph =
                new java.util.ArrayList[cells];

        for (int i = 0; i < cells; i++) {
            graph[i] = new java.util.ArrayList<>();
        }

        for (int k = 0; k < w.a().length; k++) {
            int a = w.a()[k];
            int b = w.b()[k];
            int cost = w.cost()[k];

            graph[a].add(new int[]{cost, b});
            graph[b].add(new int[]{cost, a});
        }
        visited[0] = true;

        for (int[] edge : graph[0]) {
            pq.add(edge);
        }

        int visitedCount = 1;

        while (!pq.isEmpty() && visitedCount < cells) {
            int[] edge = pq.poll();

            int cost = edge[0];
            int to = edge[1];

            if (visited[to]) {
                continue;
            }

            visited[to] = true;
            visitedCount++;
            totalCost += cost;

            for (int[] next : graph[to]) {
                if (!visited[next[1]]) {
                    pq.add(next);
                }
            }
        }

        return totalCost;
    }
}
