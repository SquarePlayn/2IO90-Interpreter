package simulator;

import exceptions.SimulatorException;

/**
 * Created by s168110 on 1-12-2017.
 */
public class SimulatorReport {

    private boolean success;
    private long runTime;
    private float costs;

    private SimulatorException exception;

    public SimulatorReport(boolean success, long runTime, float costs) {

        this.success = success;
        this.runTime = runTime;
        this.costs = costs;

    }

    public SimulatorReport(boolean success, SimulatorException exception) {
        this(success, -1, -1);
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

    public String getReaason() {
        return exception.getMessage();
    }
}
