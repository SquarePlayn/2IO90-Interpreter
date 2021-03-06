package preamble;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;

/**
 * preamble.Preamble object to store all preamble information
 */
public class Preamble {

    private double alpha;
    private int maximumTravelTime;
    private int amountOfTaxis;
    private int taxiCapacity;
    private Graph graph;
    private int trainingPeriod;
    private int callListLength;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public int getMaximumTravelTime() {
        return maximumTravelTime;
    }

    public void setMaximumTravelTime(int maximumTravelTime) {
        this.maximumTravelTime = maximumTravelTime;
    }

    public int getAmountOfTaxis() {
        return amountOfTaxis;
    }

    public void setAmountOfTaxis(int amountOfTaxis) {
        this.amountOfTaxis = amountOfTaxis;
    }

    public int getTaxiCapacity() {
        return taxiCapacity;
    }

    public void setTaxiCapacity(int taxiCapacity) {
        this.taxiCapacity = taxiCapacity;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public int getTrainingPeriod() {
        return trainingPeriod;
    }

    public void setTrainingPeriod(int trainingPeriod) {
        this.trainingPeriod = trainingPeriod;
    }

    public int getCallListLength() {
        return callListLength;
    }

    public void setCallListLength(int callListLength) {
        this.callListLength = callListLength;
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
            }
        }

        trainingPeriod = Integer.parseInt(input.get(0).split(" ")[0]);
        callListLength = Integer.parseInt(input.remove(0).split(" ")[1]);

    }

}
