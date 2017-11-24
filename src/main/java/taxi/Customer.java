package taxi;

import graph.Vertex;

/**
 *
 */
public class Customer {

    private Vertex destination;
    private int age;
    private int initialDistance;

    public Customer(Vertex start, Vertex destination) {
        this.destination = destination;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getAge() {
        return age;
    }
}
