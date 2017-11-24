package taxi;

import graph.Vertex;

import java.util.ArrayList;

/**
 *
 */
public class Taxi {

    public static int capacity;

    private Vertex position;
    private ArrayList<Customer> passangers;

    public Taxi(Vertex position) {
        this.position = position;
        this.passangers = new ArrayList<>();
    }

    public Vertex getPosition() {
        return position;
    }

    public void setPosition(Vertex position) {
        this.position = position;
    }

    public boolean pickup() {

        return false;

    }
}
