package graph;

import java.util.ArrayList;
import java.util.Arrays;

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

    private void createVertices(int amountOfVertices) {

        for (int i = 0; i < amountOfVertices; i++) {
            vertices.add(new Vertex(i));
        }

    }

}
