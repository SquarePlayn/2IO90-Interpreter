package simulator;

import exceptions.SimulatorException;
import taxi.Taxi;

import java.util.ArrayList;

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
     * All taxis that were used in the algorithm
     */
    private ArrayList<Taxi> taxis;

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
            int maximumTimeReached,
            ArrayList<Taxi> taxis
    ) {

        this.success = true;
        this.runTime = runTime;
        this.costs = costs;
        this.maximumTimeReached = maximumTimeReached;
        this.taxis = taxis;

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

    /**
     * This method calculates data about how many custoemrs were in a taxi at a given time. The result
     * is returned in a integer array with the following definitions at each index:
     * 0 = average per minute, including taxis with 0 customers
     * 1 = average per minute, excluding taxi with 0 customers
     * 2 = maximum number of passenger in all taxis
     * 3 = TODO
     *
     * @return Data array
     */
    public float[] getCustomerCount() {

        int totalIncluding = 0;
        int totalIncludingCount = 0;

        int totalExcluding = 0;
        int totalExcludingCount = 0;

        int maximumPassengers = Integer.MIN_VALUE;

        for (Taxi taxi : taxis) {
            for (int customerCount : taxi.getCustomerAmountHistory()) {

                // Customer count is the amount of passengers in the taxi at each minute
                if (customerCount == 0) {
                    totalIncludingCount++;
                } else {
                    totalIncluding += customerCount;
                    totalIncludingCount++;

                    totalExcluding += customerCount;
                    totalExcludingCount++;
                }

                if (customerCount > maximumPassengers) {
                    maximumPassengers = customerCount;
                }
            }
        }

        float averageIncluding = (float) totalIncluding / (float) totalIncludingCount;
        float averageExcluding = (float) totalExcluding / (float) totalExcludingCount;

        return new float[]{
                averageIncluding,
                averageExcluding,
                maximumPassengers
        };
    }

    public String getReason() {
        return exception.getMessage();
    }
}
