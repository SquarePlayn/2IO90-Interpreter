package graph;

import javafx.util.Pair;
import taxi.Customer;

import java.util.*;

/**
 *
 */
public class Graph {

    private ArrayList<Vertex> vertices;

    public Graph(int amountOfVertices) {

        vertices = new ArrayList<Vertex>();
        createVertices(amountOfVertices);

    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

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

            System.out.println("Customers");
            for (Customer customer : vertex.getCustomers()) {
                System.out.println("Customer ID: " + customer.getId());
                System.out.println("    Start: " + customer.getStartLocation().getId());
                System.out.println("    Destination: " + customer.getDestination().getId());
                System.out.println("    Age: " + customer.getAge());
            }
        }

    }

    private void createVertices(int amountOfVertices) {

        for (int i = 0; i < amountOfVertices; i++) {
            vertices.add(new Vertex(i));
        }

    }

}
