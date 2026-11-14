package entity;

import control.InvalidActionException;

// The cashier collects payments.
public class Cashier extends Staff {
    public boolean processPayment(Customer customer, Restaurant restaurant, int turn) throws InvalidActionException {
        ensureCanAct();
        if (customer == null || !customer.canPay(turn)) {
            throw new InvalidActionException("Customer cannot pay this turn.");
        }
        restaurant.recordPayment(customer);
        useTask();
        return true;
    }

    @Override
    public String getRoleName() {
        return "Cashier";
    }
}
