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

    public boolean move(Vertex destination) {
        if (position.getNeighbours().contains(destination)) {
            position = destination;
            return true;
        } else {
            // TODO Provide feedback
            return false;
        }
    }

    public boolean pickup(Vertex destination) {

        if (passangers.size() >= capacity) {
            // TODO Provide feedback
            return false;
        }

        Customer customer = position.getCustomer(destination);

        if (customer == null) {
            // TODO Provide feedback
            return false;
        }

        position.removeCustomer(customer);
        passangers.add(customer);

        return true;
    }

    public boolean drop(Vertex destination) {

        Customer candidate = null;

        for (Customer customer : passangers) {

            if (customer.getDestination() == destination) {
                if (candidate == null) {
                    candidate = customer;
                } else {
                    if (customer.getAge() > candidate.getAge()) {
                        candidate = customer;
                    }
                }
            }
        }

        if (candidate == null) {
            return false;
        }

        passangers.remove(candidate);

        if (candidate.getDestination() == position) {
            candidate.setArrivedAtLocation(true);
        } else {
            position.addCustomer(candidate);
        }

        return true;
    }
}
