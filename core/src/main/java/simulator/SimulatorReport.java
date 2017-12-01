package simulator;

/**
 * Created by s168110 on 1-12-2017.
 */
public class SimulatorReport {

    private boolean success;
    private long runTime;
    private float costs;

    public SimulatorReport(boolean success, long runTime, float costs) {

        this.success = success;
        this.runTime = runTime;
        this.costs = costs;

    }

    public SimulatorReport(boolean success) {
        this(success, -1, -1);
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
}
