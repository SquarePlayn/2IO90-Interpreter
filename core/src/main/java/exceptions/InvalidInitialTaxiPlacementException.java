package exceptions;

import java.util.Arrays;

/**
 *
 */
public class InvalidInitialTaxiPlacementException extends SimulatorException {

    public InvalidInitialTaxiPlacementException(String command, String[] line) {
        super(
                "Invalid command encountered while reading the first line of the output in the"
                + " real call list \n"
                + "Command: " + command
                + "Line: " + Arrays.toString(line)
        );
    }
}
