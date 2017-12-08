package exceptions;

/**
 *
 */
public class UnknownTaxiException extends SimulatorException {

    public UnknownTaxiException(int id) {
        super(
                "Could not find taxi with ID: " + id
        );
    }

}
