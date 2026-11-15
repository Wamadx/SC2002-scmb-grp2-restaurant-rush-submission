package entity;

import enums.CustomerStatus;
import java.util.List;

// Customer status and satisfaction, the order, and when satisfaction drops, the customer leaves, and when they pay.
public class Customer {
    public static final int MAX_SATISFACTION = 100;
    private static final int REGULAR_WAITING_LOSS = 8;
    private static final int EATING_TURNS = 1;

    private int id;
    private int satisfaction = MAX_SATISFACTION;
    private CustomerStatus status;
    private int arrivalTurn;
    private int eatingTurnsLeft;
    private int readyToPayTurn = -1;
    private Table table;
    private Order order;

    public Customer(int id, int arrivalTurn) {
        this.id = id;
        this.satisfaction = MAX_SATISFACTION;
        this.status = CustomerStatus.WAITING;
        this.arrivalTurn = arrivalTurn;
        this.eatingTurnsLeft = 0;
    }

    public MenuItem chooseFood(Menu m) {
        List<MenuItem> items = m.getItems();
        // Deterministic rule (Stage 3, four menu items): customer n orders items.get(n % 4).
        // So customers 3, 7, 11... order the combo. Critics always order Pasta (see CriticCustomer).
        return items.get(id % items.size());
    }

    public String getTypeName() {
        return "Regular";
    }

    public int getWaitingLoss() {
        return REGULAR_WAITING_LOSS;
    }

    public void recoverSatisfaction(int pts) {
        this.satisfaction = Math.min(MAX_SATISFACTION, this.satisfaction + pts);
    }

    // Regular customers pay the full menu price. VIP customers override this.
    public int calculateBill(MenuItem item) {
        return item.getPrice();
    }

    public boolean isWaitingForService() {
        return status == CustomerStatus.WAITING
                || status == CustomerStatus.SEATED
                || status == CustomerStatus.ORDERED;
    }

    public void applyWaitingLoss() {
        if (isWaitingForService()) {
            decreaseSatisfaction(getWaitingLoss());
        }
    }

    public void decreaseSatisfaction(int pts) {
        this.satisfaction = Math.max(0, this.satisfaction - pts);
    }

    public boolean isAbandoned() {
        return this.satisfaction <= 0 || this.status == CustomerStatus.ABANDONED;
    }

    public void seatAt(Table table) {
        this.table = table;
        this.status = CustomerStatus.SEATED;
    }

    public void placeOrder(Order order) {
        this.order = order;
        this.status = CustomerStatus.ORDERED;
    }

    public void receiveFood() {
        this.status = CustomerStatus.SERVED;
        this.eatingTurnsLeft = EATING_TURNS;
    }

    public void startEatingIfServed() {
        if (status == CustomerStatus.SERVED) {
            status = CustomerStatus.EATING;
        }
    }

    public void continueEating(int turn) {
        if (status != CustomerStatus.EATING) {
            return;
        }
        decrementEatingTurnsLeft();
        if (eatingTurnsLeft <= 0) {
            status = CustomerStatus.READY_TO_PAY;
            readyToPayTurn = turn;
        }
    }

    public boolean canPay(int turn) {
        return status == CustomerStatus.READY_TO_PAY && readyToPayTurn < turn;
    }

    public void markPaid() {
        this.status = CustomerStatus.PAID;
        this.table = null;
    }

    public void markAbandoned() {
        this.status = CustomerStatus.ABANDONED;
        this.table = null;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    private int decrementEatingTurnsLeft() {
        if (eatingTurnsLeft > 0) {
            eatingTurnsLeft--;
        }
        return eatingTurnsLeft;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return "Customer " + id + " (" + getTypeName() + ")";
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public Table getTable() {
        return table;
    }

    public Order getOrder() {
        return order;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    public boolean hasBeenServed() {
        return status == CustomerStatus.SERVED
                || status == CustomerStatus.EATING
                || status == CustomerStatus.READY_TO_PAY
                || status == CustomerStatus.PAID;
    }
}
