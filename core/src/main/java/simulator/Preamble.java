package simulator;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

/**
 * simulator.Preamble object to store all preamble information
 */
public class Preamble {

    private double alpha;
    private int maximumTravelTime;
    private int amountOfTaxis;
    private int taxiCapacity;
    private Graph graph;
    private int trainingPeriodLength;
    private int callListLength;
    private int edgeCount;

    public double getAlpha() {
        return alpha;
    }

    public int getMaximumTravelTime() {
        return maximumTravelTime;
    }

    public int getAmountOfTaxis() {
        return amountOfTaxis;
    }

    public int getTaxiCapacity() {
        return taxiCapacity;
    }

    public Graph getGraph() {
        return graph;
    }

    public int getTrainingPeriodLength() {
        return trainingPeriodLength;
    }

    public int getCallListLength() {
        return callListLength;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    /**
     * Reads the input from the preamble
     *
     * @param input
     */
    public void read(ArrayList<String> input) {

        // Remove the amount of preamble lines left from the input
        input.remove(0);

        alpha = Double.parseDouble(input.remove(0));
        maximumTravelTime = Integer.parseInt(input.remove(0));
        amountOfTaxis = Integer.parseInt(input.get(0).split(" ")[0]);
        taxiCapacity = Integer.parseInt(input.remove(0).split(" ")[1]);

        int amountOfVertices = Integer.parseInt(input.remove(0));

        graph = new Graph(amountOfVertices);
        edgeCount = 0;

        // Loop over each 'vertex' in the input
        for (int i = 0; i < amountOfVertices; i++) {

            // Split the line and extract the amount of edges
            String[] edgesInput = input.remove(0).split(" ");
            int amountOfEdges = Integer.parseInt(edgesInput[0]);

            // Loop over the ids of the edges on the line
            for (int j = 0; j < amountOfEdges; j++) {
                Vertex current = graph.getVertex(i);
                Vertex neighbour = graph.getVertex(Integer.parseInt(edgesInput[j + 1]));
                current.addNeighbour(neighbour);
                edgeCount++;
            }
        }

        // Every edge is counted twice
        edgeCount /= 2;

        trainingPeriodLength = Integer.parseInt(input.get(0).split(" ")[0]);
        callListLength = Integer.parseInt(input.remove(0).split(" ")[1]);

    }

}
