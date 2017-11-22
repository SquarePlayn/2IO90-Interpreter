import input.Reader;

import java.util.ArrayList;

/**
 * Main entry point for the interpreter
 */
public class Interpreter {

    private Reader inputReader;
    private Reader outputReader;

    private ArrayList<String> input;
    private ArrayList<String> output;

    private Preamble preamble;

    public Interpreter() {

        input = new ArrayList<String>();
        output = new ArrayList<String>();

        inputReader = new Reader(input);
        outputReader = new Reader(output);

        preamble = new Preamble();

    }

    public void run() {

        // TODO Update to register input and output reader
        // IMPORTANT This is broken for now, TaxiScanner must first be updated
        TaxiScanner.getInstance().registerReader(inputReader);

        // Runs the algorithm
        (new Main()).run();



    }

    public static void main(String[] args) {
        (new Interpreter()).run();
    }
}
