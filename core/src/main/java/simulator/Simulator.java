package simulator;

import exceptions.InvalidInitialTaxiPlacementException;
import exceptions.SimulatorException;
import graph.Graph;
import graph.Vertex;
import taxi.Customer;
import taxi.Taxi;

import java.util.ArrayList;

public class Simulator {

    private static Simulator instance;

    private ArrayList<String> input;
    private ArrayList<String> output;

    private Preamble preamble;
    private Graph graph;
    private ArrayList<Customer> customers;

    private float costs;

    public Simulator(ArrayList<String> input, ArrayList<String> output) {

        this.input = input;
        this.output = output;
        this.preamble = new Preamble();
        this.customers = new ArrayList<>();
        this.costs = 0;

    }

    public boolean simulate() {

        // Run setup
        setup();

        // Go through training period
        simulateTrainingPeriod();

        // Get initial taxi placement
        try {
            setInitialTaxisPosition();
        } catch (SimulatorException exception) {
            // TODO Handle error

            return false;
        }

        while (output.size() > 0) {

            // Read a line from the input
            parseInput();

            // Read a line from the output
            try {
                parseOutput();
            } catch (SimulatorException exception) {

                // TODO Handle error
                return false;
            }

            // Up the age of each customer by 1
            ageCustomers();

        }

        calculateCosts();

        return true;

    }

    public float getCosts() {
        return costs;
    }

    private void calculateCosts() {

        for (Customer customer : customers) {
            double customerCost = customer.getAge() / Math.pow(customer.getInitialDistance() + 2, preamble.getAlpha());
            costs += customerCost;
        }

    }

    private void setInitialTaxisPosition() throws SimulatorException {

        // Check if all taxis are placed, if not, throw error
        String[] line = output.remove(0).split(" ");

        int pointer = 0;
        boolean isDone = false;

        do {

            String command = line[pointer];

            if (command.equals("m")) {

                // Create a new taxi at the desired location
                int taxiId = Integer.parseInt(line[pointer + 1]);
                int vertexId = Integer.parseInt(line[pointer + 2]);

                Vertex start = graph.getVertex(vertexId);

                Taxi.create(taxiId, start);

                pointer += 3;

            } else if (command.equals("c")) {
                isDone = true;
            } else {
                throw new InvalidInitialTaxiPlacementException(command, line);
            }

        } while (!isDone);

    }

    private void parseOutput() throws SimulatorException {

        // Represents a line from the output
        String[] line = output.remove(0).split(" ");

        int pointer = 0;
        boolean isDone = false;

        // TODO Change to for loop and check if the line is ended with a c
        do {

            String command = line[pointer];

            if (command.equals("c")) {
                return;
            }

            int taxiId = Integer.parseInt(line[pointer + 1]);
            int destinationId = Integer.parseInt(line[pointer + 2]);

            Taxi taxi = Taxi.getTaxi(taxiId);
            Vertex destination = graph.getVertex(destinationId);

            switch (command) {

                case "p":
                    taxi.pickup(destination);
                    break;

                case "m":
                    taxi.move(destination);
                    break;

                case "d":
                    taxi.drop(destination);
                    break;

                default:
                    // TODO Error
                    break;

            }

            pointer += 3;

        } while (!isDone);

    }

    /**
     * Increases the age of all customers by one, if they have not arrived at their destination
     */
    private void ageCustomers() {

        for (Customer customer : customers) {
            customer.age();
        }

    }

    /**
     * Parses one line from the input
     */
    private void parseInput() {

        if (input.size() <= 0) {
            return;
        }

        // Line represents the call list
        String[] line = input.remove(0).split(" ");

        int amountOfCalls = Integer.parseInt(line[0]);

        // Go through each call
        for (int i = 0; i < amountOfCalls; i++) {

            // Get the ids of the start and end vertices
            int startLocationId = Integer.parseInt(line[2 * i + 1]);
            int destinationId = Integer.parseInt(line[2 * i + 2]);

            // Get the corresponding vertices from the graph
            Vertex start = graph.getVertex(startLocationId);
            Vertex destination = graph.getVertex(destinationId);

            // Create a new customer
            Customer customer = new Customer(start, destination);

            // Store the customer
            customers.add(customer);
            graph.getVertex(startLocationId).addCustomer(customer);
        }

    }

    private void simulateTrainingPeriod() {

        for (int i = 0; i < preamble.getTrainingPeriodLength(); i++) {

            // Just skip the training period, for now
            output.remove(0);
            input.remove(0);

        }

    }

    /**
     * Setup required variables
     */
    private void setup() {

        // Read the preamble
        preamble.read(input);
        graph = preamble.getGraph();

        // Set information in models for easier access
        Customer.graph = graph;
        Taxi.capacity = preamble.getTaxiCapacity();
        Taxi.maximumNumberOfTaxis = preamble.getAmountOfTaxis();

    }

}
