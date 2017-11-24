import graph.Graph;
import input.Reader;
import org.apache.commons.cli.*;
import taxi.*;
import taxi.Customer;
import taxi.Taxi;

import java.io.File;
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

    private ArrayList<Taxi> taxis;

    public Interpreter() {

        input = new ArrayList<String>();
        output = new ArrayList<String>();

        inputReader = new Reader(input);
        outputReader = new Reader(output);

        preamble = new Preamble();

    }

    public void run() {

        TaxiScanner.getInstance().registerInputReader(inputReader);
        TaxiScanner.getInstance().registerOutputReader(outputReader);

        // Runs the algorithm
        (new Main()).run();

        preamble.read(input);
        Customer.graph = preamble.getGraph();

        taxi.Taxi.capacity = preamble.getTaxiCapacity();

        // Read input and output, simulate actions

    }

    public static void main(String[] args) {
        Options options = new Options(); // Use apache commons cli api to create options.
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        boolean justAlgorithm = false;
        boolean printHelp = false;
        File inputFile = null;

        options.addOption("h", "help", false, "Prints this help message.");
        options.addOption("a", "just-algorithm", false,
                "Just run the algorithm, without running the interpreter.");
        options.addOption("i", "input", true, "Input file.");

        try {
            cmd = parser.parse(options, args);
            printHelp = cmd.hasOption("h");
            justAlgorithm = cmd.hasOption("a");
            inputFile = cmd.hasOption("i") ? new File(cmd.getOptionValue("i")) : null;
        } catch (ParseException e) {
            System.out.println("Error while parsing arguments, continuing normal execution.");
            e.printStackTrace();
        }
        
        if (printHelp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "interpreter", options );
            return;
        }

        if (inputFile != null) {
            System.out.println("Using specified input file.");
            TaxiScanner.setInputFile(inputFile);
        }

        if (justAlgorithm) {
            System.out.println("Running just the algorithm...");
            (new Main()).run();
            return;
        }

        System.out.println("Executing interpreter");
        (new Interpreter()).run();
    }
}
