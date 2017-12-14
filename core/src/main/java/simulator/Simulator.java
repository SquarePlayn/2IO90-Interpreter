package simulator;

import exceptions.InvalidInitialTaxiPlacementException;
import exceptions.InvalidOutputException;
import exceptions.SimulatorException;
import graph.Graph;
import graph.Vertex;
import taxi.Customer;
import taxi.Taxi;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    private static Simulator instance;

    private ArrayList<String> input;
    private ArrayList<String> output;

    private Preamble preamble;
    private Graph graph;
    private ArrayList<Customer> customers;

    /* Metrics */

    /**
     * Total costs calculated according to the cost function
     */
    private float costs;

    /**
     * List of all customers that took longer to get from their start to their destination
     * thatn was allowed by the maximum travel time
     */
    private List<Customer> lateCustomer;

    /**
     * Number of calls handled
     */
    private int callsHandled;

    /**
     * Constructor.
     *
     * @param input Input file data
     * @param output Output data of the algorithm
     */
    public Simulator(ArrayList<String> input, ArrayList<String> output) {

        this.input = input;
        this.output = output;
        this.preamble = new Preamble();
        this.customers = new ArrayList<>();
        this.lateCustomer = new ArrayList<>();
        this.costs = 0;

    }

    /**
     * @return Total costs as defined by the cost function
     */
    public float getCosts() {
        return costs;
    }

    /**
     * @return All late customers
     */
    public List<Customer> getLateCustomer() {
        return lateCustomer;
    }

    public Preamble getPreamble() {
        return preamble;
    }

    public int getCallsHandled() {
        return callsHandled;
    }

    public void simulate() throws SimulatorException {

        // Run setup
        setup();

        // Go through training period
        simulateTrainingPeriod();

        // Get initial taxi placement
        setInitialTaxisPosition();

        while (output.size() > 0) {

            // Reset the turn type such that each taxi can pickup/drop/move freely again as a first move
            for (Taxi taxi : Taxi.getTaxis()) {
                taxi.resetTurnType(); // TODO Combine
                taxi.update();
            }

            // Read a line from the input
            parseInput();

            // Read a line from the output
            parseOutput();

            // Up the age of each customer by 1
            ageCustomers();

        }

        calculateCosts();

        checkMaximumTravelTimeReached();
    }

    private void calculateCosts() {

        for (Customer customer : customers) {
            double customerCost = (float) customer.getAge() / (float) Math.pow(customer.getInitialDistance() + 2, preamble.getAlpha());
            customerCost = Math.pow(customerCost, 2);
            costs += customerCost;
        }

    }

    /**
     * Adds all the customers that arrived too late at their destination to a list.
     * A customer arrives late when their travel it took longer than the maximum allowed travel
     * time
     */
    private void checkMaximumTravelTimeReached() {

        for (Customer customer : customers) {
            if (customer.getAge() >= preamble.getMaximumTravelTime()) {
                lateCustomer.add(customer);
            }
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

        // Check if all taxis are placed in the graph
        if (Taxi.getTaxis().size() != preamble.getAmountOfTaxis()) {
            throw new InvalidInitialTaxiPlacementException("Did not place all taxis in the 0th minute");
        }

    }

    private void parseOutput() throws SimulatorException {

        // Represents a line from the output
        String fullLine = output.remove(0);
        String[] line = fullLine.split(" ");

        for (int pointer = 0; pointer < line.length; pointer += 3) {

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
                    throw new InvalidOutputException("Encountered an unknown command symbol '"
                        + command + "' on line '" + fullLine + "'");

            }
        }

        // Did not encounter a 'c'
        throw new InvalidOutputException("Line was not ended with a 'c' for line '" + fullLine + "'");
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
        callsHandled += amountOfCalls;

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
