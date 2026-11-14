package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaitingQueue {
    private List<Customer> customers;

    public WaitingQueue() {
        this.customers = new ArrayList<>();
    }

    public void add(Customer customer) {
        customers.add(customer);
    }

    public void remove(Customer customer) {
        customers.remove(customer);
    }

    public boolean contains(Customer customer) {
        return customers.contains(customer);
    }

    public boolean isEmpty() {
        return customers.isEmpty();
    }

    public List<Customer> getAll() {
        return Collections.unmodifiableList(customers);
    }
}
