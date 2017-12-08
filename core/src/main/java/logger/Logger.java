package logger;

/**
 *
 */
public class Logger {

    private boolean enableOutput;

    public Logger(boolean enableOutput) {
        this.enableOutput = enableOutput;
    }

    public Logger() {
        this(false);
    }

    public void info(String message) {
        log(message, "info");
    }

    public void warn(String message) {
        log(message, "warn");
    }

    public void error(String message) {
        log(message, "error");
    }

    private void log(String message, String type) {
        if (enableOutput) {
            System.out.println("[" + type + "]  " + message);
        }
    }
}
