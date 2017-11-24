import graph.*;
import input.Reader;
import taxi.*;

import java.util.ArrayList;

/**
 * Main entry point for the interpreter
 */
public class Interpreter {

    private Reader inputReader;
    private Reader outputReader;

    private ArrayList<String> input;
    private ArrayList<String> output;

    private Preamble preamble;

    private ArrayList<Taxi> taxis;

    public Interpreter() {

        input = new ArrayList<String>();
        output = new ArrayList<String>();

        inputReader = new Reader(input);
        outputReader = new Reader(output);

        preamble = new Preamble();

    }

    public void run() {

        graph.Graph graph = new graph.Graph(5);
        graph.Vertex vertex0 = graph.getVertex(0);
        graph.Vertex vertex1 = graph.getVertex(1);
        graph.Vertex vertex2 = graph.getVertex(2);
        graph.Vertex vertex3 = graph.getVertex(3);
        graph.Vertex vertex4 = graph.getVertex(4);

        vertex0.addNeighbour(vertex1);
        vertex0.addNeighbour(vertex2);

        vertex1.addNeighbour(vertex0);
        vertex1.addNeighbour(vertex2);

        vertex2.addNeighbour(vertex0);
        vertex2.addNeighbour(vertex1);
        vertex2.addNeighbour(vertex3);
        vertex2.addNeighbour(vertex4);

        vertex3.addNeighbour(vertex2);
        vertex3.addNeighbour(vertex4);

        vertex4.addNeighbour(vertex2);
        vertex4.addNeighbour(vertex3);


        System.exit(0);

        TaxiScanner.getInstance().registerInputReader(inputReader);
        TaxiScanner.getInstance().registerOutputReader(outputReader);

        // Runs the algorithm
        (new Main()).run();

        preamble.read(input);

        taxi.Taxi.capacity = preamble.getTaxiCapacity();


    }

    public static void main(String[] args) {
        (new Interpreter()).run();
    }
}
