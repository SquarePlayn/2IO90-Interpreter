package graph;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 */
public class Vertex {

    private int id;
    private ArrayList<Vertex> neighbours;

    public Vertex(int id) {
        this.id = id;
        this.neighbours = new ArrayList<Vertex>();
    }

    public void addNeighbour(Vertex vertex) {
        neighbours.add(vertex);
    }

}
