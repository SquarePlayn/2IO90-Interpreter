package graph;

import taxi.Customer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Graph representation
 */
public class Graph {

    /**
     * List of all vertices in the graph. The index of the vertex in this list corresponds to its ID
     */
    private ArrayList<Vertex> vertices;

    /**
     * Constructor.
     *
     * @param amountOfVertices The graph size
     */
    public Graph(int amountOfVertices) {

        vertices = new ArrayList<Vertex>();
        createVertices(amountOfVertices);

    }

    /**
     * @param id ID of a vertex
     * @return The vertex with the given ID
     */
    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    /**
     * @return The size of the graph
     */
    public int getSize() {
        return vertices.size();
    }

    /**
     * @param start Vertex to start from
     * @param end Goal vertex
     * @return The minimal distance between the start and goal vertices
     */
    public int getDistance(Vertex start, Vertex end) {

        ArrayList<Vertex> queue = new ArrayList<Vertex>();
        HashMap<Vertex, Integer> visited = new HashMap<Vertex, Integer>();

        visited.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {

            Vertex now = queue.remove(0);
            int distanceNow = visited.get(now);

            for (Vertex neighbour : now.getNeighbours()) {

                if (neighbour == end) {
                    return distanceNow + 1;
                }

                if (!visited.containsKey(neighbour)) {

                    visited.put(neighbour, distanceNow + 1);
                    queue.add(neighbour);

                }
            }
        }

        return -1;
    }

    public void debug() {

        System.out.println("Graph information ");
        System.out.println("-----------------");
        for (Vertex vertex : vertices) {

            System.out.println("Vertex ID: " + vertex.getId());
            System.out.println("Neighbours");
            for (Vertex neighbour : vertex.getNeighbours()) {
                System.out.println("    Vertex ID: " + neighbour.getId());
            }

            System.out.println("    Customers");
            for (Customer customer : vertex.getCustomers()) {
                System.out.println("        Customer ID: " + customer.getId());
                System.out.println("        Start: " + customer.getStartLocation().getId());
                System.out.println("        Destination: " + customer.getDestination().getId());
                System.out.println("        Age: " + customer.getAge());
            }
        }

    }

    private void createVertices(int amountOfVertices) {

        for (int i = 0; i < amountOfVertices; i++) {
            vertices.add(new Vertex(i));
        }

    }

}
