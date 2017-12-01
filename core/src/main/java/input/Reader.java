package input;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 *
 */
public class Reader implements Consumer<String> {

    private ArrayList<String> buffer;

    public Reader(ArrayList<String> buffer) {

        this.buffer = buffer;

    }

    @Override
    public void accept(String string) {
        buffer.add(string);
    }
}
