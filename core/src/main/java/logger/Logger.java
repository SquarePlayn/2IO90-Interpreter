package logger;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Logger class for logging information
 */
public class Logger {

    /**
     * If true, output will be written to the output buffer (default console)
     */
    private boolean enableOutput;

    /**
     * Constructor.
     *
     * @param enableOutput Flag for enabled output
     */
    public Logger(boolean enableOutput) {
        this.enableOutput = enableOutput;
    }

    /**
     * Constructor.
     * <p>
     * Creates a logger that does not produce any output
     */
    public Logger() {
        this(false);
    }

    public void info(String message) {
        log(message, Level.INFO);
    }

    /**
     * Logs a message with warning log level
     *
     * @param message The message to log
     */
    public void warn(String message) {
        log(message, Level.WARNING);
    }

    /**
     * Logs a message with error log level
     *
     * @param message The message to log
     */
    public void error(String message) {
        log(message, Level.ERROR);
    }

    /**
     * Prints a separator in the console
     */
    public void separator() {
        log("-----------------------", Level.INFO);
    }

    /**
     * Formats a integer value to a more readable format
     *
     * @param value Incoming integer value
     * @return Readable integer format
     */
    public String formatInteger(int value) {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(value).replace(",", " ");
    }

    /**
     * Formats a float value to a more readable format
     *
     * @param value Incoming float value
     * @return Readable float format
     */
    public String formatFloat(float value) {
        return String.format("%,.2f", value).replace(".", " ").replace(",", ".");
    }

    /**
     * Formats a time given in milliseconds to a more readable format
     *
     * @param time Time in milliseconds
     * @return Readable time format
     */
    public String formatTime(long time) {

        if (time < 2000) {
            return formatInteger((int) time) + " ms";
        }

        return formatFloat(((float) time) / 1000) + " seconds";
    }

    /**
     * Logs to the console with a specified level indication
     *
     * @param message Message to log
     * @param level   Level to log at
     */
    private void log(String message, Level level) {
        if (enableOutput) {
            System.out.println("[" + level.getPresentation() + "]  " + message);
        }
    }

    private enum Level {

        INFO(0, "info"),
        WARNING(1, "warning"),
        ERROR(2, "error");

        private final int level;
        private final String presentation;

        Level(final int level, final String presentation) {
            this.level = level;
            this.presentation = presentation;
        }

        public int getLevel() {
            return level;
        }

        public String getPresentation() {
            return presentation;
        }

    }
}
