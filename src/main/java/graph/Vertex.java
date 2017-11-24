package graph;

import taxi.Customer;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 */
public class Vertex {

    private int id;
    private ArrayList<Vertex> neighbours;
    private ArrayList<Customer> customers;

    public Vertex(int id) {
        this.id = id;
        this.neighbours = new ArrayList<Vertex>();
        this.customers = new ArrayList<Customer>();
    }

    public int getId() {
        return id;
    }

    public void addNeighbour(Vertex vertex) {
        neighbours.add(vertex);
    }

    public ArrayList<Vertex> getNeighbours() {
        return neighbours;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public Customer getCustomer(Vertex destination) {

        Customer candidate = null;

        for (Customer customer : customers) {
            if (customer.getDestination() == destination) {
                if (candidate == null) {
                    candidate = customer;
                } else {
                    if (customer.getAge() > candidate.getAge()) {
                        candidate = customer;
                    } else if (customer.getAge() == candidate.getAge()) {
                        if (customer.getInitialDistance() < candidate.getInitialDistance()) {
                            candidate = customer;
                        }
                    }
                }
            }
        }

        return candidate;
    }
}
