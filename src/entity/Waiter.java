package entity;

import control.InvalidActionException;
import enums.CustomerStatus;
import enums.OrderStatus;

// The waiter's actions: seating customers, taking orders, and serving food.
public class Waiter extends Staff {
    private int nextOrderId = 101;

    public boolean seatCustomer(Customer customer, Table table, WaitingQueue queue) throws InvalidActionException {
        ensureCanAct();
        if (!queue.contains(customer)) {
            throw new InvalidActionException(customer.getLabel() + " is not in the waiting queue.");
        }
        if (table == null || !table.isFree()) {
            throw new InvalidActionException("Table is not free.");
        }
        queue.remove(customer);
        table.seat(customer);
        customer.seatAt(table);
        useTask();
        return true;
    }

    public Order takeOrder(Customer customer, Menu menu, int turn) throws InvalidActionException {
        ensureCanAct();
        if (customer.getStatus() != CustomerStatus.SEATED) {
            throw new InvalidActionException(customer.getLabel() + " must be seated without an order to place one.");
        }
        MenuItem chosenItem = customer.chooseFood(menu);
        Order order = new Order(nextOrderId++, chosenItem, customer);
        customer.placeOrder(order);
        useTask();
        return order;
    }

    public boolean serve(Order order, int turn) throws InvalidActionException {
        ensureCanAct();
        if (!order.isReady()) {
            throw new InvalidActionException("Order #" + order.getId() + " is not READY yet.");
        }
        if (order.getReadyTurn() >= turn) {
            throw new InvalidActionException("Order #" + order.getId() + " became READY this turn and can only be served next turn.");
        }
        order.setStatus(OrderStatus.SERVED);
        order.getOwner().receiveFood();
        useTask();
        return true;
    }

    @Override
    public String getRoleName() {
        return "Waiter";
    }
}
