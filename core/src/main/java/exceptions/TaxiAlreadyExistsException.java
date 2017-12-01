package exceptions;

/**
 * Created by s168110 on 1-12-2017.
 */
public class TaxiAlreadyExistsException extends SimulatorException {

    public TaxiAlreadyExistsException(int id) {
        super(
                "A taxi already exists with ID: " + id
        );
    }

}
