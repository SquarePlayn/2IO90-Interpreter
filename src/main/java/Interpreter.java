/**
 * Created by Marijn van der Horst on 22-11-2017.
 */
public class Interpreter {
    private Reader reader = new Reader();

    public void run() {
        TaxiScanner.getInstance().registerReader(reader);
    }

    public static void main(String[] args) {
        (new Interpreter()).run();
    }
}
