package taxi;

import graph.Vertex;
import graph.Graph;

/**
 *
 */
public class Customer {

    public static Graph graph;

    private Vertex destination;
    private int age;
    private int initialDistance;

    public Customer(Vertex start, Vertex destination) {
        this.destination = destination;
        initialDistance = graph.getDistance(start, destination);
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getInitialDistance() {
        return initialDistance;
    }

    public int getAge() {
        return age;
    }
}
