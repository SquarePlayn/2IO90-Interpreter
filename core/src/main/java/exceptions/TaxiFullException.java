package exceptions;

import graph.Vertex;
import taxi.Taxi;

/**
 *
 */
public class TaxiFullException extends InterpreterException {

    public TaxiFullException(Taxi taxi, Vertex destination) {

        super("Tried to pick up a customer in a taxi which is already full \n"
                + "Taxi ID: " + taxi.getId() + "\n"
                + "Taxi position: " + taxi.getPosition() + "\n"
                + "Destination ID of customer: " + destination.getId());

    }

}
