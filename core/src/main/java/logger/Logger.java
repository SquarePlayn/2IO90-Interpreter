package logger;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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

    public String formatInteger(int value) {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(value).replace(",", " ");
    }

    public String formatFloat(float value) {
        return String.format("%,.2f", value).replace(".", " ").replace(",", ".");
    }

    public String formatTime(long time) {

        if (time < 2000) {
            return formatInteger((int) time) + " ms";
        }

        return formatFloat(((float) time) / 1000) + " seconds";
    }

    private void log(String message, String type) {
        if (enableOutput) {
            System.out.println("[" + type + "]  " + message);
        }
    }
}
