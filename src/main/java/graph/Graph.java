package graph;

import javafx.util.Pair;

import java.util.*;

/**
 *
 */
public class Graph {

    private ArrayList<Vertex> vertices;

    public Graph(int amountOfVertices) {

        createVertices(amountOfVertices);

    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    public int getDistance(Vertex start, Vertex end) {

        Queue<Vertex> queue = new PriorityQueue<>();
        HashMap<Vertex, Integer> visited = new HashMap<Vertex, Integer>();

        visited.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {

            Vertex now = queue.poll();
            int distanceNow = visited.get(now);

            for (Vertex neighbour : now.getNeighbours()) {

                if (neighbour == end) {
                    return distanceNow + 1;
                }

                if (!visited.containsKey(now)) {

                    visited.put(neighbour, distanceNow + 1);
                    queue.add(neighbour);

                }
            }
        }

        return -1;
    }

    private void createVertices(int amountOfVertices) {

        for (int i = 0; i < amountOfVertices; i++) {
            vertices.add(new Vertex(i));
        }

    }

}
