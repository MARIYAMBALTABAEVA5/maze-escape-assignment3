
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

}

