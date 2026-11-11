package entity;

import control.InvalidActionException;
import enums.CustomerStatus;
import enums.OrderStatus;

// The cashier collects payments.
public class Cashier extends Staff {
    public boolean processPayment(Customer customer, Restaurant restaurant, int turn) throws InvalidActionException {
        ensureCanAct();
        if (customer == null || !customer.canPay(turn)) {
            throw new InvalidActionException("Customer cannot pay this turn.");
        }
        Order order = customer.getOrder();
        restaurant.addRevenue(order.getBill());
        restaurant.addPaidCustomer(customer);
        customer.setStatus(CustomerStatus.PAID);
        order.setStatus(OrderStatus.PAID);
        if (customer.getTable() != null) {
            customer.getTable().release();
        }
        customer.markPaid();
        useTask();
        return true;
    }

    @Override
    public String getRoleName() {
        return "Cashier";
    }
}
