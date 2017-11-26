package taxi;

import graph.Vertex;
import graph.Graph;

/**
 *
 */
public class Customer {

    public static Graph graph;
    public static int customerCounter = 0;

    private int id;
    private Vertex startLocation;
    private Vertex destination;
    private int age;
    private int initialDistance;
    private boolean arrivedAtLocation;

    public Customer(Vertex start, Vertex destination) {
        this.id = customerCounter++;
        this.startLocation = start;
        this.destination = destination;
        this.initialDistance = graph.getDistance(start, destination);
        this.age = 0;
        this.arrivedAtLocation = false;
    }

    public int getId() {
        return id;
    }

    public Vertex getStartLocation() {
        return startLocation;
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

    public boolean isArrivedAtLocation() {
        return arrivedAtLocation;
    }

    public void setArrivedAtLocation(boolean arrivedAtLocation) {
        this.arrivedAtLocation = arrivedAtLocation;
    }
}
