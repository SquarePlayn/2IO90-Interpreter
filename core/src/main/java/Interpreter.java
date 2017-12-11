import CLI.CommandLineProcessor;
import config.ConfigParser;
import exceptions.SimulatorException;
import input.Reader;
import logger.Logger;
import org.apache.commons.cli.HelpFormatter;
import simulator.Simulator;
import simulator.SimulatorReport;
import taxi.Taxi;
import testfactory.TestFactory;
import testfactory.preamble.PreambleOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point for the interpreter
 */
public class Interpreter {

    private Logger logger;

    private ArrayList<String> input;
    private ArrayList<String> output;

    public Interpreter(Logger logger) {
        this.logger = logger;
    }

    /**
     * Initialise buffers and readers to receive input and output from the algorithm
     */
    private void setup() {

        // Clear buffers
        input = new ArrayList<>();
        output = new ArrayList<>();

        // Create new readers
        Reader inputReader = new Reader(input);
        Reader outputReader = new Reader(output);

        // Register readers to get input and output from the algorithm
        TaxiScanner.getInstance().registerInputReader(inputReader);
        TaxiScanner.getInstance().registerOutputReader(outputReader);

        // Reset interpreter variables
        Taxi.reset();

    }

    private void printAverages(List<SimulatorReport> successReports, List<SimulatorReport> failedReports) {

        // Calculate totals
        float totalRunTime = 0;
        float totalCosts = 0;

        for (SimulatorReport report : successReports) {
            totalRunTime += report.getRunTime();
            totalCosts += report.getCosts();
        }

        // Output results
        logger.separator();
        logger.info("Amount of test cases run = " + (successReports.size() + failedReports.size()));
        logger.info("Successful test cases = " + successReports.size());
        if (failedReports.size() > 0) {
            logger.info("Failed test cases     = " + failedReports.size());
        }

        logger.separator();
        logger.info("Average run time = " + (totalRunTime / (float) successReports.size()));
        logger.info("Average costs    = " + (totalCosts) / (float) successReports.size());

    }

    /**
     * Runs the algorithm with a give test case file
     *
     * @param testCase Test case
     */
    private SimulatorReport sunSimulation(File testCase) {

        // Set test case
        TaxiScanner.setInputFile(testCase);
        if (!TaxiScanner.getInstance().init()) {
            return new SimulatorReport(
                    new SimulatorException("TaxiScanner.init() failed. Could not create input stream from file")
            );
        }

        // Run setup
        setup();

        // Keep track of time and run the algorithm
        long startTime = System.currentTimeMillis();
        (new Main()).run();
        long endTime = System.currentTimeMillis();

        // Reset everything in the algorithm
        Main.reset();
        TaxiScanner.getInstance().finish();

        // Create a new simulator and run simulation
        Simulator simulator = new Simulator(input, output);
        SimulatorReport report;

        try {

            simulator.simulate();

            report = new SimulatorReport(
                    endTime - startTime,
                    simulator.getCosts(),
                    simulator.getLateCustomer().size(),
                    Taxi.getTaxis()
            );

        } catch (SimulatorException exception) {

            report = new SimulatorReport(
                    exception
            );

        }

        return report;
    }

    private SimulatorReport runTestCase(File testCase, int repeatAmount) {

        logger.separator();
        logger.info("Running test case " + testCase.getPath());

        List<SimulatorReport> successReports = new ArrayList<>();
        List<SimulatorReport> failedReports = new ArrayList<>();

        // Run the test case
        for (int i = 0; i < repeatAmount; i++) {
            SimulatorReport report = sunSimulation(testCase);

            if (report.isSuccess()) {
                successReports.add(report);

                // Output information
                logger.info("Test case : Successful");
                logger.separator();

                logger.info("Run time = " + logger.formatTime(report.getRunTime()));
                logger.info("Costs    = " + logger.formatFloat(report.getCosts()));
                if (report.getMaximumTimeReached() > 0) {
                    logger.info("Amount of customers that were not delivered in time: " + report.getMaximumTimeReached());
                }

                float[] customerCountData = report.getCustomerCount();
                logger.info("Average amount of customers per taxi (including 0) = " + customerCountData[0]);
                logger.info("Average amount of customers per taxi (excluding 0) = " + customerCountData[1]);
                logger.info("Maximum number of passengers in all taxis = " + customerCountData[2]);

            } else {
                failedReports.add(report);

                logger.error("Test case : Failed");
                logger.info(report.getReason());
            }
        }

        if (repeatAmount > 1) {
            printAverages(successReports, failedReports);
        }

        return successReports.get(0);
    }

    private void runTestCases(File folder) {

        List<SimulatorReport> successReports = new ArrayList<>();
        List<SimulatorReport> failedReports = new ArrayList<>();

        for (final File testCase : folder.listFiles()) {
            if (testCase == null || testCase.isDirectory()) {
                continue;
            }

            // Run the test case
            SimulatorReport report = runTestCase(testCase, 1);

            if (report.isSuccess()) {
                successReports.add(report);
            } else {
                failedReports.add(report);
            }
        }

        printAverages(successReports, failedReports);
    }

    private void generateTestCase(File testFactoryConfig, int repeatAmount) {

        ConfigParser parser = new ConfigParser(logger, testFactoryConfig);
        PreambleOptions options = new PreambleOptions();
        TestFactory testFactory = new TestFactory();

        // General settings
        int seed = parser.getIntValue("general", "seed");

        // Graph settings
        int amountOfNodes = parser.getIntValue("graph", "amount_of_nodes");
        float density = parser.getFloatValue("graph", "density");

        options.setGraphSize(amountOfNodes);
        options.setGraphDensity(density);

        // Taxi settings
        float alpha = parser.getFloatValue("taxi", "alpha");
        int amountOfTaxis = parser.getIntValue("taxi", "amount");
        int maxDropOffTime = parser.getIntValue("taxi", "max_drop_off_time");
        int capacity = parser.getIntValue("taxi", "capacity");

        options.setAlpha(alpha);
        options.setAmountOfTaxis(amountOfTaxis);
        options.setMaxDropoffTime(maxDropOffTime);
        options.setMaxTaxiCapacity(capacity);

        // Call list settings
        int trainingPeriodLength = parser.getIntValue("call_list", "length_training_period");
        int callListLength = parser.getIntValue("call_list", "length_call_list");
        float callDensity = parser.getFloatValue("call_list", "call_list_density");
        float callFrequency = parser.getFloatValue("call_list", "call_frequency");

        options.setTrainingDuration(trainingPeriodLength);
        options.setCallListLength(callListLength);
        options.setCallDensity(callDensity);
        options.setCallFrequency(callFrequency);

        // Create the test case
        File testCase = testFactory.createTestCase(
                parser.getStringValue("general", "generated_file_output_path"),
                options,
                seed
        );

        runTestCase(testCase, repeatAmount);
    }

    public void generateMultipleTestCases(File testFactoryConfig, int amount) {

        ConfigParser parser = new ConfigParser(logger, testFactoryConfig);
        PreambleOptions options = new PreambleOptions();
        TestFactory testFactory = new TestFactory();

        List<SimulatorReport> successReports = new ArrayList<>();
        List<SimulatorReport> failedReports = new ArrayList<>();

        // General settings
        int seed = parser.getIntValue("general", "seed");

        for (int i = 0; i < amount; i++) {

            logger.info("");
            logger.info("Generating test case...");
            logger.info("Test case properties");

            // Graph settings
            int amountOfNodes = parser.getIntBulk("graph", "amount_of_nodes");
            float density = parser.getFloatBulk("graph", "density");

            options.setGraphSize(amountOfNodes);
            options.setGraphDensity(density);

            logger.info("Amount of nodes = " + amountOfNodes);
            logger.info("Graph density = " + density);

            // Taxi settings
            float alpha = parser.getFloatBulk("taxi", "alpha");
            int amountOfTaxis = parser.getIntBulk("taxi", "amount");
            int maxDropOffTime = parser.getIntBulk("taxi", "max_drop_off_time");
            int capacity = parser.getIntBulk("taxi", "capacity");

            options.setAlpha(alpha);
            options.setAmountOfTaxis(amountOfTaxis);
            options.setMaxDropoffTime(maxDropOffTime);
            options.setMaxTaxiCapacity(capacity);

            logger.info("Alpha = " + alpha);
            logger.info("Amount of taxis = " + amountOfTaxis);
            logger.info("Maximum drop off time = " + maxDropOffTime);
            logger.info("Taxi capacity = " + capacity);

            // Call list settings
            int trainingPeriodLength = parser.getIntBulk("call_list", "length_training_period");
            int callListLength = parser.getIntBulk("call_list", "length_call_list");
            float callDensity = parser.getFloatBulk("call_list", "call_list_density");
            float callFrequency = parser.getFloatBulk("call_list", "call_frequency");

            options.setTrainingDuration(trainingPeriodLength);
            options.setCallListLength(callListLength);
            options.setCallDensity(callDensity);
            options.setCallFrequency(callFrequency);

            logger.info("Training period length = " + trainingPeriodLength);
            logger.info("Call list length = " + callListLength);
            logger.info("Call density = " + callDensity);
            logger.info("Call frequency = " + callFrequency);

            // Create the test case
            File testCase = testFactory.createTestCase(
                    parser.getStringValue("general", "generated_file_output_path"),
                    options,
                    seed
            );

            SimulatorReport report = runTestCase(testCase, 1);

            if (report.isSuccess()) {
                successReports.add(report);
            } else {
                failedReports.add(report);
            }

        }

        printAverages(successReports, failedReports);
    }

    public static void main(String[] args) {

        Logger logger = new Logger(true);
        CommandLineProcessor processor = new CommandLineProcessor(args);
        Interpreter interpreter = new Interpreter(logger);

        // Parse the command line arguments
        processor.parse();

        // Check whether we want output to be send to the console
        TaxiScanner.setOutputToConsole(processor.getOutputToConsole());

        switch (processor.getExecutionMode()) {

            case HELP_MESSAGE:
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("interpreter", processor.getOptions());
                break;

            case SPECIFIED_INPUT_FILE:
                File input = processor.getInputFile();

                if (input.isDirectory()) {

                    // The input is a folder containing test cases
                    logger.info("Directory selected, running interpreter on each file");
                    interpreter.runTestCases(input);

                } else {

                    interpreter.runTestCase(input, processor.getRepeatAmount());

                }
                break;

            case GENERATED_TEST_CASE:
                File testFactoryConfig = processor.getInputFile();
                logger.info("Running algorithm with a generated test case");
                interpreter.generateTestCase(testFactoryConfig, processor.getRepeatAmount());
                break;

            case BULK_TESTING:
                testFactoryConfig = processor.getInputFile();
                logger.info("Run bulk test cases with specified configuration file");
                interpreter.generateMultipleTestCases(testFactoryConfig, processor.getRepeatAmount());
                break;

            case NONE:
                break;

        }

        logger.info("Done");
    }
}
