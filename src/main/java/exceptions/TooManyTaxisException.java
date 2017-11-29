package exceptions;

/**
 *
 */
public class TooManyTaxisException extends InterpreterException {

    public TooManyTaxisException(int id) {

        super(
                "Tried to create a taxi with a higher id than is allowed \n"
                + "ID: " + id
        );

    }

}
