package exceptions;

import graph.Vertex;
import taxi.Taxi;

/**
 *
 */
public class NoCustomerWithDestinationException extends SimulatorException {

    public NoCustomerWithDestinationException(Taxi taxi, Vertex vertex) {

        super("Could not find a customer with specified destination at position \n"
            + "Taxi ID: " + taxi.getId() + "\n"
            + "Taxi position vertex ID: " + taxi.getPosition().getId() + "\n"
            + "Destination vertex ID: " + vertex.getId());

    }

}
