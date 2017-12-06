package exceptions;

import graph.Vertex;
import taxi.Taxi;

/**
 *
 */
public class IllegalMoveException extends SimulatorException {

    public IllegalMoveException(Taxi taxi, Vertex destination) {

        super(
                "Tried to move taxi to a non-neighbouring vertex \n"
                + "Taxi ID: " + taxi.getId() + "\n"
                + "Taxi location vertex ID: " + taxi.getPosition().getId() + "\n"
                + "Destination Vertex ID: " + destination.getId()
        );

    }

    public IllegalMoveException(String message) {
        super(message);
    }
}
