import CLI.CommandLineProcessor;
import config.ConfigParser;
import exceptions.SimulatorException;
import input.Reader;
import logger.Logger;
import org.apache.commons.cli.HelpFormatter;
import simulator.Simulator;
import simulator.SimulatorReport;
import testfactory.TestFactory;
import testfactory.preamble.PreambleOptions;

import java.io.File;
import java.util.ArrayList;

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

    }

    /**
     * Runs the algorithm with a give test case file
     *
     * @param testCase Test case
     */
    private SimulatorReport runTestCase(File testCase) {

        // Set test case
        TaxiScanner.setInputFile(testCase);
        if (!TaxiScanner.getInstance().init()) {
            return new SimulatorReport(
                    false,
                    new SimulatorException("TaxiScanner.init() failed. Could not create input stream from file")
            );
        }

        // Run setup
        setup();

        // Keep track of time and run the algorithm
        long startTime = System.currentTimeMillis();
        (new Main()).run();
        long endTime = System.currentTimeMillis();

        // Create a new simulator and run simulation
        Simulator simulator = new Simulator(input, output);
        SimulatorReport report;

        try {

            simulator.simulate();

            report = new SimulatorReport(
                    true,
                    endTime - startTime,
                    simulator.getCosts()
            );

        } catch (SimulatorException exception) {

            report = new SimulatorReport(
                    false,
                    exception
            );

        }

        return report;
    }

    public void runSingleTestCase(File testCase) {

        SimulatorReport report = runTestCase(testCase);

        if (!report.isSuccess()) {
            logger.error("Test case unsuccessful");
            logger.info(report.getReaason());
        } else {
            logger.info("Test case successful");

            logger.info("Run time = " + report.getRunTime() + "ms");
            logger.info("Costs    = " + report.getCosts());
            // TODO Print more metric data
        }

    }

    public void generateTestCase(File testFactoryConfig) {

        ConfigParser parser = new ConfigParser(logger, testFactoryConfig);
        PreambleOptions options = new PreambleOptions();
        TestFactory testFactory = new TestFactory();

        // General
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

        options.setTrainingDuration(trainingPeriodLength);
        options.setCallListLength(callListLength);

        File testCase = testFactory.createTestCase(
                "temp/test.txt",
                options,
                seed
        );

        runSingleTestCase(testCase);
    }

    public static void main(String[] args) {

        Logger logger = new Logger(true);
        CommandLineProcessor processor = new CommandLineProcessor(args, logger);
        Interpreter interpreter = new Interpreter(logger);

        // Parse the command line arguments
        processor.parse();

        // Check whether we want output to be send to the console
        TaxiScanner.setOutputToConsole(processor.getOutputToConsole());

        switch (processor.getExecutionMode()) {

            case HELP_MESSAGE:
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "interpreter", processor.getOptions());
                break;

            case SPECIFIED_INPUT_FILE:
                File testCase = processor.getInputFile();
                logger.info("Running algorithm on specified test case file; " + testCase.getAbsolutePath());
                interpreter.runSingleTestCase(testCase);
                break;

            case GENERATED_TEST_CASE:
                File testFactoryConfig = processor.getInputFile();
                logger.info("Running algorithm with a generated test case");
                interpreter.generateTestCase(testFactoryConfig);
                break;

            case BULK_TESTING:
                break;

            case NONE:
                break;

        }

        logger.info("Done");

    }
}
