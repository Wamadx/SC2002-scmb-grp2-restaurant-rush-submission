package entity;

import enums.CustomerStatus;
import enums.OrderStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// The restaurant's overall records: revenue, paid customers, unhappy departures, and the tables.
public class Restaurant {
    private String name;
    private List<Table> tables;
    private WaitingQueue waitingQueue;
    private List<Order> orders;
    private List<Customer> customers;
    private Menu menu;
    private Waiter waiter;
    private Chef chef;
    private Cashier cashier;
    private List<Staff> staff;
    private List<Customer> paidCustomers;
    private List<Integer> paidSatisfactions;
    private double revenue;
    private int abandonedCount;

    public Restaurant(String name) {
        this(name, 2, Menu.createStandardMenu());
    }

    public Restaurant(String name, int tableCount, Menu menu) {
        this.name = name;
        this.menu = menu;
        this.tables = new ArrayList<>();
        for (int i = 1; i <= tableCount; i++) {
            tables.add(new Table(i));
        }
        this.waitingQueue = new WaitingQueue();
        this.orders = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.waiter = new Waiter();
        this.chef = new Chef();
        this.cashier = new Cashier();
        this.staff = Arrays.asList(waiter, chef, cashier);
        this.paidCustomers = new ArrayList<>();
        this.paidSatisfactions = new ArrayList<>();
        this.revenue = 0.0;
        this.abandonedCount = 0;
    }

    public void addArrival(Customer customer) {
        customers.add(customer);
        waitingQueue.add(customer);
    }

    public Table getFreeTable() {
        for (Table table : tables) {
            if (table.isFree()) {
                return table;
            }
        }
        return null;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void recordPayment(Customer customer) {
        if (customer.getStatus() == CustomerStatus.PAID) {
            return;
        }
        addRevenue(customer.getOrder().getBill());
        customer.getOrder().setStatus(OrderStatus.PAID);
        if (customer.getTable() != null) {
            customer.getTable().release();
        }
        customer.markPaid();
        addPaidCustomer(customer);
    }

    public void recordAbandonment(Customer customer) {
        if (customer.getStatus() == CustomerStatus.ABANDONED
                || customer.getStatus() == CustomerStatus.PAID) {
            return;
        }
        waitingQueue.remove(customer);
        if (customer.getTable() != null) {
            customer.getTable().release();
        }
        if (customer.getOrder() != null) {
            customer.getOrder().setStatus(OrderStatus.CANCELLED);
        }
        customer.markAbandoned();
        abandonedCount++;
    }

    public Customer findNextCustomerToPay(int turn) {
        Customer next = null;
        for (Customer c : customers) {
            if (c.canPay(turn)) {
                if (next == null || c.getReadyToPayTurn() < next.getReadyToPayTurn()) {
                    next = c;
                }
            }
        }
        return next;
    }

    public List<Customer> getActiveCustomers() {
        List<Customer> active = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getStatus() != CustomerStatus.PAID && c.getStatus() != CustomerStatus.ABANDONED) {
                active.add(c);
            }
        }
        return active;
    }

    public List<Customer> getCustomersWaitingToOrder() {
        List<Customer> result = new ArrayList<>();
        for (Table t : tables) {
            if (!t.isFree() && t.getOccupant().getStatus() == CustomerStatus.SEATED) {
                result.add(t.getOccupant());
            }
        }
        return result;
    }

    public List<Order> getOrdersNeedingPreparation() {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.needsPreparation()) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Order> getReadyOrders() {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.isReady()) {
                result.add(o);
            }
        }
        return result;
    }

    public int getReadyToPayCount() {
        int count = 0;
        for (Customer c : customers) {
            if (c.getStatus() == CustomerStatus.READY_TO_PAY) {
                count++;
            }
        }
        return count;
    }

    public int getServedCount() {
        int count = 0;
        for (Customer c : customers) {
            if (c.hasBeenServed()) {
                count++;
            }
        }
        return count;
    }

    public double getAverageSatisfaction() {
        if (paidSatisfactions.isEmpty()) {
            return -1.0;
        }
        int sum = 0;
        for (int sat : paidSatisfactions) {
            sum += sat;
        }
        return (double) sum / paidSatisfactions.size();
    }

    public void addRevenue(double amount) {
        this.revenue += amount;
    }

    public void addPaidCustomer(Customer customer) {
        this.paidCustomers.add(customer);
        this.paidSatisfactions.add(customer.getSatisfaction());
    }

    public Menu getMenu() {
        return menu;
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public WaitingQueue getWaitingQueue() {
        return waitingQueue;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Chef getChef() {
        return chef;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getPaidCount() {
        return paidCustomers.size();
    }

    public int getAbandonedCount() {
        return abandonedCount;
    }

    public String getName() {
        return name;
    }
}
