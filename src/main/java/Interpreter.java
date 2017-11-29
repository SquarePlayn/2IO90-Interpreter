import exceptions.InterpreterException;
import graph.Graph;
import graph.Vertex;
import input.Reader;
import org.apache.commons.cli.*;
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

    private Graph graph;

    private ArrayList<Customer> customers;

    public Interpreter() {

        input = new ArrayList<>();
        output = new ArrayList<>();

        inputReader = new Reader(input);
        outputReader = new Reader(output);

        preamble = new Preamble();

        customers = new ArrayList<>();

    }

    private void run() {

        TaxiScanner.getInstance().registerInputReader(inputReader);
        TaxiScanner.getInstance().registerOutputReader(outputReader);

        // Runs the algorithm
        System.out.println("Running algorithm");
        (new Main()).run();
        System.out.println("Done running the algorithm");

        preamble.read(input);

        graph = preamble.getGraph();
        Customer.graph = preamble.getGraph();
        taxi.Taxi.capacity = preamble.getTaxiCapacity();
        Taxi.maximumNumberOfTaxis = preamble.getAmountOfTaxis();

        System.out.println("Starting simulation");

        int lineNumber = 1;

        // Read input and output, simulate actions
        // TODO Check if maximum number of output lines have been passed
        while (output.size() > 0) {

            if (lineNumber++ <= preamble.getTrainingPeriod()) {
                output.remove(0);
                input.remove(0);
                continue;
            }

            System.out.println("Parsing output: " + output.get(0));

            try {
                parseOutput(output.remove(0).split(" "));
            } catch (InterpreterException exception) {
                System.out.println(exception.getMessage());
                System.exit(1);
            }

            for (Customer customer : customers) {
                customer.age();
            }

            if (input.size() > 0) {
                System.out.println("Parsing input: " + input.get(0));
                String[] inputLine = input.remove(0).split(" ");
                parseCallList(inputLine);
            }

        }

        // Calculate costs
        double costs = 0;

        for (Customer customer : customers) {

            double customerCost = customer.getAge() / Math.pow(customer.getInitialDistance() + 2, preamble.getAlpha());
            costs += customerCost;

        }

        System.out.println("Costs: " + costs);

        System.out.println("Done");

    }

    /**
     * Reads the input line. If customer calls are present, create new customers and add
     * them to their start locations.
     *
     * @param callList Input line
     */
    private void parseCallList(String[] callList) {

        int amountOfCalls = Integer.parseInt(callList[0]);

        for (int i = 0; i < amountOfCalls; i++) {

            int startLocationId = Integer.parseInt(callList[2 * i + 1]);
            int destinationId = Integer.parseInt(callList[2 * i + 2]);

            Vertex start = graph.getVertex(startLocationId);
            Vertex destination = graph.getVertex(destinationId);

            Customer customer = new Customer(start, destination);

            customers.add(customer);
            graph.getVertex(startLocationId).addCustomer(customer);

        }
    }

    private void parseOutput(String[] commands) throws InterpreterException {

        boolean isDone = false;
        int pointer = 0;

        // TODO Refactor into more beautiful code
        do {
            if (commands[pointer].equals("p")) {

                int taxiId = Integer.parseInt(commands[pointer + 1]);
                int destinationId = Integer.parseInt(commands[pointer + 2]);

                Taxi taxi = Taxi.getTaxi(taxiId);
                Vertex destination = graph.getVertex(destinationId);

                taxi.pickup(destination);

                pointer += 3;
                continue;

            }

            if (commands[pointer].equals("m")) {

                int taxiId = Integer.parseInt(commands[pointer + 1]);
                int destinationId = Integer.parseInt(commands[pointer + 2]);

                Taxi taxi = Taxi.getTaxi(taxiId);
                Vertex destination = graph.getVertex(destinationId);

                taxi.move(destination);

                pointer += 3;
                continue;

            }

            if (commands[pointer].equals("d")) {

                int taxiId = Integer.parseInt(commands[pointer + 1]);
                int destinationId = Integer.parseInt(commands[pointer + 2]);

                Taxi taxi = Taxi.getTaxi(taxiId);
                Vertex destination = graph.getVertex(destinationId);

                taxi.drop(destination);

                pointer += 3;
                continue;

            }

            if (commands[pointer].equals("c")) {

                isDone = true;

            }

        } while (!isDone);

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
