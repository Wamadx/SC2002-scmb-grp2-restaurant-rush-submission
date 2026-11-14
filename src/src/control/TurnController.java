package control;

import boundary.ConsoleUI;
import entity.*;
import enums.CustomerStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Control the flow of each turn.
public class TurnController {
    private static final List<Integer> ARRIVAL_TURNS = Arrays.asList(1, 4, 7, 10, 13, 16);
    private ConsoleUI ui;
    private Restaurant restaurant;
    private int nextCustomerId = 1;

    public TurnController(Restaurant restaurant, ConsoleUI ui) {
        this.restaurant = restaurant;
        this.ui = ui;
    }

    public void executeTurn(int turn) {
        startTurn();
        ui.displayTurnHeader(turn);
        processArrival(turn);
        ui.displayStartOfTurn(turn, restaurant);
        processWaiterAction(turn);
        processChefAction(turn);
        processEatingAndPayment(turn);
        applyWaitingLoss();
        showEndOfTurn(turn);
    }

    private void startTurn() {
        for (Staff s : restaurant.getStaff()) {
            s.resetForTurn();
        }
        for (Customer c : restaurant.getActiveCustomers()) {
            c.startEatingIfServed();
        }
    }

    private void processArrival(int turn) {
        if (ARRIVAL_TURNS.contains(turn)) {
            Customer customer = createArrival(nextCustomerId++, turn);
            restaurant.addArrival(customer);
            ui.displayMessage(">> " + customer.getLabel()
                    + " arrives and joins the waiting queue (satisfaction 100).");
        }
    }

    // vip come at 4 and critic come at 10, rest are normal
    private Customer createArrival(int id, int turn) {
        if (turn == 4) {
            return new VIPCustomer(id, turn);
        }
        if (turn == 10) {
            return new CriticCustomer(id, turn);
        }
        return new Customer(id, turn);
    }

    private void processWaiterAction(int turn) {
        List<String> options = new ArrayList<>();
        options.add("Seat a waiting customer");
        options.add("Take a seated customer's order");
        options.add("Serve a READY order");

        while (true) {
            int choice = ui.readChoice("Waiter - choose ONE task:", options, "Wait");
            try {
                boolean done;
                switch (choice) {
                    case 0:
                        ui.displayMessage("Waiter waits this turn.");
                        return;
                    case 1:
                        done = seatAction();
                        break;
                    case 2:
                        done = takeOrderAction(turn);
                        break;
                    default:
                        done = serveAction(turn);
                        break;
                }
                if (done) {
                    return;
                }
            } catch (InvalidActionException e) {
                ui.displayMessage("Invalid choice: " + e.getMessage() + " Please choose again.");
            }
        }
    }

    private boolean seatAction() throws InvalidActionException {
        List<Customer> waiting = new ArrayList<>(restaurant.getWaitingQueue().getAll());
        if (waiting.isEmpty()) {
            throw new InvalidActionException("No customer is waiting in the queue.");
        }
        Table table = restaurant.getFreeTable();
        if (table == null) {
            throw new InvalidActionException("All tables are occupied.");
        }
        List<String> labels = new ArrayList<>();
        for (Customer c : waiting) {
            labels.add(c.getLabel() + " (satisfaction " + c.getSatisfaction() + ")");
        }
        int pick = ui.readChoice("Seat which customer at Table " + table.getId() + "?", labels, "Back");
        if (pick == 0) {
            return false;
        }
        Customer customer = waiting.get(pick - 1);
        restaurant.getWaiter().seatCustomer(customer, table, restaurant.getWaitingQueue());
        ui.displayMessage("Waiter seats " + customer.getLabel() + " at Table " + table.getId() + ".");
        return true;
    }

    private boolean takeOrderAction(int turn) throws InvalidActionException {
        List<Customer> seated = restaurant.getCustomersWaitingToOrder();
        if (seated.isEmpty()) {
            throw new InvalidActionException("No seated customer is waiting to order.");
        }
        List<String> labels = new ArrayList<>();
        for (Customer c : seated) {
            labels.add(c.getLabel() + " at Table " + c.getTable().getId()
                    + " (satisfaction " + c.getSatisfaction() + ")");
        }
        int pick = ui.readChoice("Take whose order?", labels, "Back");
        if (pick == 0) {
            return false;
        }
        Customer customer = seated.get(pick - 1);
        Order order = restaurant.getWaiter().takeOrder(customer, restaurant.getMenu(), turn);
        restaurant.addOrder(order);
        ui.displayMessage("Waiter takes " + customer.getLabel() + "'s order: "
                + order.getItem().getName() + " (" + ConsoleUI.formatMoney(order.getBill())
                + ", " + order.getItem().getPrepUnits() + " preparation unit(s)).");
        return true;
    }

    private boolean serveAction(int turn) throws InvalidActionException {
        List<Order> ready = restaurant.getReadyOrders();
        if (ready.isEmpty()) {
            throw new InvalidActionException("No order is READY to serve.");
        }
        List<String> labels = new ArrayList<>();
        for (Order o : ready) {
            labels.add("Order #" + o.getId() + " " + o.getItem().getName()
                    + " for " + o.getOwner().getLabel());
        }
        int pick = ui.readChoice("Serve which order?", labels, "Back");
        if (pick == 0) {
            return false;
        }
        Order order = ready.get(pick - 1);
        restaurant.getWaiter().serve(order, turn);
        ui.displayMessage("Waiter serves Order #" + order.getId() + " to "
                + order.getOwner().getLabel() + ": SERVED.");
        return true;
    }

    private void processChefAction(int turn) {
        List<Order> toPrepare = restaurant.getOrdersNeedingPreparation();
        if (toPrepare.isEmpty()) {
            ui.displayMessage("Chef has no order to prepare and waits.");
            return;
        }
        List<String> labels = new ArrayList<>();
        for (Order o : toPrepare) {
            labels.add("Order #" + o.getId() + " " + o.getItem().getName() + " for "
                    + o.getOwner().getLabel() + " (" + o.getProgressUnits() + "/"
                    + o.getItem().getPrepUnits() + " units done)");
        }
        while (true) {
            int pick = ui.readChoice("Chef - advance which order by one unit?", labels, "Wait");
            if (pick == 0) {
                ui.displayMessage("Chef waits this turn.");
                return;
            }
            Order order = toPrepare.get(pick - 1);
            try {
                restaurant.getChef().prepare(order, turn);
                String result = order.isReady()
                        ? "READY (can be served next turn)"
                        : order.getProgressUnits() + "/" + order.getItem().getPrepUnits() + " units done";
                ui.displayMessage("Chef works on Order #" + order.getId() + ": " + result + ".");
                return;
            } catch (InvalidActionException e) {
                ui.displayMessage("Invalid choice: " + e.getMessage() + " Please choose again.");
            }
        }
    }

    private void processEatingAndPayment(int turn) {
        Customer payer = restaurant.findNextCustomerToPay(turn);
        if (payer != null) {
            try {
                int bill = payer.getOrder().getBill();
                restaurant.getCashier().processPayment(payer, restaurant, turn);
                ui.displayMessage("Cashier collects " + ConsoleUI.formatMoney(bill) + " from "
                        + payer.getLabel() + ": PAID (satisfaction " + payer.getSatisfaction()
                        + " recorded), table freed.");
            } catch (InvalidActionException e) {
                ui.displayMessage("Cashier could not charge: " + e.getMessage());
            }
        }
        for (Customer c : restaurant.getActiveCustomers()) {
            if (c.getStatus() == CustomerStatus.EATING) {
                c.continueEating(turn);
                if (c.getStatus() == CustomerStatus.READY_TO_PAY) {
                    ui.displayMessage(c.getLabel() + " finishes eating: READY_TO_PAY (pays next turn at the earliest).");
                }
            }
        }
    }

    private void applyWaitingLoss() {
        for (Customer c : restaurant.getActiveCustomers()) {
            c.applyWaitingLoss();
            if (c.isAbandoned()) {
                restaurant.recordAbandonment(c);
                ui.displayMessage("!! " + c.getLabel()
                        + " reached satisfaction 0 and leaves without paying.");
            }
        }
    }

    private void showEndOfTurn(int turn) {
        ui.displayEndOfTurn(turn, restaurant);
    }
}
