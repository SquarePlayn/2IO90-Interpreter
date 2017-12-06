import CLI.CommandLineProcessor;
import exceptions.SimulatorException;
import input.Reader;
import logger.Logger;
import org.apache.commons.cli.*;
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
        }

    }

    private void testTestFactory() {
        // TODO Rewrite this
        TestFactory testFactory = new TestFactory();
        PreambleOptions options = new PreambleOptions();

        options.setAlpha(0.5);
        options.setAmountOfTaxis(10);
        options.setMaxDropoffTime(10000);
        options.setMaxTaxiCapacity(4);
        options.setGraphSize(1000);
        options.setTrainingDuration(10);
        options.setCallListLength(1000);
        options.setGraphDensity(0.1F);

        testFactory.createTestCase(
                "C:/Users/s163980/Documents/TU/Year 2/Quartile 2/DBL Algorithms/test.txt",
                options,
                12345678910L);
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
                logger.info("Running algorithm on specified test case file; " + testCase.getName());
                interpreter.runSingleTestCase(testCase);
                break;

            case GENERATED_TEST_CASE:
                File testFactoryConfig = processor.getInputFile();
                break;

            case BULK_TESTING:
                break;

            case NONE:
                break;

        }

        logger.info("Done");

    }
}
