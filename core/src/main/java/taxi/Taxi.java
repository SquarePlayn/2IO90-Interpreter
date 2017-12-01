package taxi;

import exceptions.*;
import graph.NullVertex;
import graph.Vertex;

import java.util.ArrayList;

/**
 *
 */
public class Taxi {

    public static int capacity;
    public static int maximumNumberOfTaxis;

    private static ArrayList<Taxi> taxis = new ArrayList<>();

    private int id;
    private Vertex position;
    private ArrayList<Customer> passangers;

    public Taxi(int id, Vertex position) {
        this.id = id;
        this.position = position;
        this.passangers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Vertex getPosition() {
        return position;
    }

    public void move(Vertex destination) throws InterpreterException {

        if (position instanceof NullVertex) {
            position = destination;
            return;
        }

        if (position.getNeighbours().contains(destination)) {
            position = destination;
        } else {
            throw new IllegalMoveException(this, destination);
        }
    }

    public void pickup(Vertex destination) throws InterpreterException {

        if (passangers.size() >= capacity) {
            throw new TaxiFullException(this, destination);
        }

        Customer customer = position.getCustomer(destination);

        if (customer == null) {
            throw new NoCustomerWithDestinationException(this, destination);
        }

        position.removeCustomer(customer);
        passangers.add(customer);
    }

    public boolean drop(Vertex destination) throws InterpreterException {

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
            throw new NoCustomerWithDestinationException(this, destination);
        }

        passangers.remove(candidate);

        if (candidate.getDestination() == position) {
            candidate.setArrivedAtLocation(true);
        } else {
            position.addCustomer(candidate);
        }

        return true;
    }

    public static Taxi getTaxi(int id) throws TooManyTaxisException {

        for (Taxi taxi : taxis) {
            if (taxi.getId() == id) {
                return taxi;
            }
        }

        if (id > maximumNumberOfTaxis) {
            throw new TooManyTaxisException(id);
        }

        Taxi taxi = new Taxi(id, new NullVertex());
        taxis.add(taxi);

        return taxi;
    }
}

