
public class Zones {

    // Mission 4: Kosaraju algorithm
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();

        int[][] graph = Graphs.slidesFrom(s);
        int[][] reverse = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int orderCount = 0;

        int[] nextEdge = new int[n];

        java.util.ArrayDeque<Integer> stack =
                new java.util.ArrayDeque<>();

        // First DFS: finishing order
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            visited[start] = true;
            stack.push(start);

            while (!stack.isEmpty()) {
                int current = stack.peek();

                if (nextEdge[current] < graph[current].length) {
                    int edge = graph[current][nextEdge[current]++];
                    int next = s.to()[edge];

                    if (!visited[next]) {
                        visited[next] = true;
                        stack.push(next);
                    }
                } else {
                    order[orderCount++] = current;
                    stack.pop();
                }
            }
        }

        // Second DFS: strongly connected components
        int[] zoneOf = new int[n];
        java.util.Arrays.fill(zoneOf, -1);

        int zoneCount = 0;

        for (int i = orderCount - 1; i >= 0; i--) {
            int start = order[i];

            if (zoneOf[start] != -1) {
                continue;
            }

            stack.push(start);
            zoneOf[start] = zoneCount;

            while (!stack.isEmpty()) {
                int current = stack.pop();

                for (int edge : reverse[current]) {
                    int next = s.from()[edge];

                    if (zoneOf[next] == -1) {
                        zoneOf[next] = zoneCount;
                        stack.push(next);
                    }
                }
            }

            zoneCount++;
        }

        return zoneOf;
    }

    // Mission 5: Longest chain and cheapest route
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {

        // Number of zones in the longest chain
        long[] stages = new long[zoneCount];
        java.util.Arrays.fill(stages, 1);

        // Minimum cost from the starting zone
        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[zoneCount];
        java.util.Arrays.fill(dist, INF);

        int startZone = zoneOf[0];
        int exitZone = zoneOf[s.n() - 1];

        dist[startZone] = 0;

        // Group outgoing slides by their starting zone
        int[] counts = new int[zoneCount];

        for (int i = 0; i < s.from().length; i++) {
            int fromZone = zoneOf[s.from()[i]];
            int toZone = zoneOf[s.to()[i]];

            if (fromZone != toZone) {
                counts[fromZone]++;
            }
        }

        int[][] edges = new int[zoneCount][];

        for (int z = 0; z < zoneCount; z++) {
            edges[z] = new int[counts[z]];
            counts[z] = 0;
        }

        for (int i = 0; i < s.from().length; i++) {
            int fromZone = zoneOf[s.from()[i]];
            int toZone = zoneOf[s.to()[i]];

            if (fromZone != toZone) {
                edges[fromZone][counts[fromZone]++] = i;
            }
        }

        // Process zones in topological order
        for (int z = 0; z < zoneCount; z++) {

            for (int edge : edges[z]) {
                int nextZone = zoneOf[s.to()[edge]];

                // Longest chain of zones
                stages[nextZone] = Math.max(
                        stages[nextZone],
                        stages[z] + 1
                );

                // Cheapest route
                if (dist[z] != INF) {
                    long newCost = dist[z] + s.time()[edge];

                    if (newCost < dist[nextZone]) {
                        dist[nextZone] = newCost;
                    }
                }
            }
        }

        long longestChain = 0;

        for (long value : stages) {
            longestChain = Math.max(longestChain, value);
        }

        return new long[]{longestChain, dist[exitZone]};
    }
}
