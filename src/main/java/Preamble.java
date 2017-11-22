import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

/**
 * Preamble object to store all preamble information
 */
public class Preamble {

    private float alpha;
    private int maximumTravelTime;
    private int amountOfTaxis;
    private int taxiCapacity;
    private Graph graph;
    private int trainingPeriod;
    private int callListLength;

    /**
     * Reads the input from the preamble
     *
     * @param input
     */
    public void read(ArrayList<String> input) {

        // Index corresponds to a line number in the input
        int index = 0;

        alpha = Float.parseFloat(input.get(index++));
        maximumTravelTime = Integer.parseInt(input.get(index++));
        amountOfTaxis = Integer.parseInt(input.get(index).split(" ")[0]);
        taxiCapacity = Integer.parseInt(input.get(index++).split(" ")[1]);

        int amountOfVertices = Integer.parseInt(input.get(index++));

        graph = new Graph(amountOfVertices);

        // Loop over each 'vertex' in the input
        for (int i = 0; i < amountOfVertices; i++) {

            // Split the line and extract the amount of edges
            String[] edgesInput = input.get(index++).split(" ");
            int amountOfEdges = Integer.parseInt(edgesInput[0]);

            // Loop over the ids of the edges on the line
            for (int j = 0; j < amountOfEdges; j++) {
                Vertex current = graph.getVertex(i);
                Vertex neighbour = graph.getVertex(Integer.parseInt(edgesInput[j + 1]));
                current.addNeighbour(neighbour);
            }
        }

        trainingPeriod = Integer.parseInt(input.get(index).split(" ")[0]);
        callListLength = Integer.parseInt(input.get(index).split(" ")[1]);

    }

}
