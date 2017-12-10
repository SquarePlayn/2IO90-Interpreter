package simulator;

import exceptions.SimulatorException;

/**
 * Data class that holds all information about a finished simulation
 */
public class SimulatorReport {

    /**
     * Indicates whether the simulation was a success or failed
     */
    private boolean success;

    /**
     * The run time of the simulation
     */
    private long runTime;

    /**
     * The total costs dictated by the cost function
     */
    private float costs;

    /**
     * The amount of customers that took too long to deliver (i.e. their travel time passed the total
     * allowed amount of travel time)
     */
    private int maximumTimeReached;

    /**
     * Exception of why the simulation failed, if that is the case
     */
    private SimulatorException exception;

    /**
     * Constructor
     *
     * @param runTime            The run time of the algorithm
     * @param costs              The result of the cost function
     * @param maximumTimeReached The amount of customers that reached the maximum travel time
     */
    public SimulatorReport(
            long runTime,
            float costs,
            int maximumTimeReached
    ) {

        this.success = true;
        this.runTime = runTime;
        this.costs = costs;
        this.maximumTimeReached = maximumTimeReached;

    }

    /**
     * Constructor used when the simulation fails
     *
     * @param exception The exception of why the simulation failed
     */
    public SimulatorReport(SimulatorException exception) {
        this.success = false;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getRunTime() {
        return runTime;
    }

    public float getCosts() {
        return costs;
    }

    public int getMaximumTimeReached() {
        return maximumTimeReached;
    }

    public String getReason() {
        return exception.getMessage();
    }
}
