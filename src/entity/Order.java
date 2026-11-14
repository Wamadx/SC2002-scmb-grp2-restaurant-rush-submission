package entity;

import enums.OrderStatus;

// The order's cooking progress and the bill amount.
public class Order {
    private int id;
    private MenuItem item;
    private Customer owner;
    private int progressUnits;
    private OrderStatus status;
    private int readyTurn;
    private int billCents;

    // bill calculate with order
    public Order(int id, MenuItem item, Customer owner) {
        this.id = id;
        this.item = item;
        this.owner = owner;
        this.progressUnits = 0;
        this.status = OrderStatus.PENDING;
        this.readyTurn = -1;
        this.billCents = owner.calculateBill(item);
    }

    public boolean needsPreparation() {
        return status == OrderStatus.PENDING || status == OrderStatus.IN_PREPARATION;
    }

    public void advance(int turn) {
        this.progressUnits++;
        if (this.progressUnits >= item.getPrepUnits()) {
            setStatus(OrderStatus.READY);
            this.readyTurn = turn;
        } else {
            setStatus(OrderStatus.IN_PREPARATION);
        }
    }

    public boolean isReady() {
        return this.status == OrderStatus.READY;
    }

    public void markServed() {
        setStatus(OrderStatus.SERVED);
    }

    public void markPaid() {
        setStatus(OrderStatus.PAID);
    }

    public void cancel() {
        setStatus(OrderStatus.CANCELLED);
    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getReadyTurn() {
        return readyTurn;
    }

    public int getId() {
        return id;
    }

    public MenuItem getItem() {
        return item;
    }

    public Customer getOwner() {
        return owner;
    }

    public int getProgressUnits() {
        return progressUnits;
    }

    // Bill in cents.
    public int getBill() {
        return billCents;
    }
}
