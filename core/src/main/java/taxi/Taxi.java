package taxi;

import com.sun.org.apache.bcel.internal.generic.SWITCH;
import exceptions.*;
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

    private TurnType turnType;

    public Taxi(int id, Vertex position) {
        this.id = id;
        this.position = position;
        this.passangers = new ArrayList<>();
        this.turnType = TurnType.NONE;
    }

    public int getId() {
        return id;
    }

    public Vertex getPosition() {
        return position;
    }

    public void resetTurnType() {
        turnType = TurnType.NONE;
    }

    public void move(Vertex destination) throws SimulatorException {
        if (turnType == TurnType.PASSENGER_SWITCH) {
            throw new IllegalMoveException("Tried to move a taxi while it already picked or dropped "
                + " off a passenger. \n Taxi ID: " + id);
        }

        turnType = TurnType.MOVE;

        if (position.getNeighbours().contains(destination)) {
            position = destination;
        } else {
            throw new IllegalMoveException(this, destination);
        }
    }

    public void pickup(Vertex destination) throws SimulatorException {

        if (turnType == TurnType.MOVE) {
            throw new IllegalMoveException("Tried pickup a passenger while the taxi already moved "
                    + "\n Taxi ID: " + id);
        }

        turnType = TurnType.PASSENGER_SWITCH;

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

    public boolean drop(Vertex destination) throws SimulatorException {

        if (turnType == TurnType.MOVE) {
            throw new IllegalMoveException("Tried drop a passenger while the taxi already moved "
                    + "\n Taxi ID: " + id);
        }

        turnType = TurnType.PASSENGER_SWITCH;

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

    public static Taxi create(int id, Vertex position) throws SimulatorException {

        // Test if a taxi with the given ID already exists
        try {
            getTaxi(id);
        } catch (UnknownTaxiException exception) {

            // Check if are not over the limit
            if (taxis.size() >= maximumNumberOfTaxis) {
                throw new TooManyTaxisException(id);
            }

            // No taxi with this ID exists, create a new one
            Taxi taxi = new Taxi(id, position);
            taxis.add(taxi);
            return taxi;

        }

        // A taxi with this is already exists, throw error
        throw new TaxiAlreadyExistsException(id);
    }

    public static ArrayList<Taxi> getTaxis() {
        return taxis;
    }

    public static Taxi getTaxi(int id) throws UnknownTaxiException {

        for (Taxi taxi : taxis) {
            if (taxi.getId() == id) {
                return taxi;
            }
        }

        // Could not find a taxi with the given id
        throw new UnknownTaxiException(id);
    }

    private enum TurnType {
        NONE,
        MOVE,
        PASSENGER_SWITCH
    }
}

