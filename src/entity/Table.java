package entity;

public class Table {
    private int id;
    private Customer occupant;

    public Table(int id) {
        this.id = id;
        this.occupant = null;
    }

    public int getId() {
        return id;
    }

    public Customer getOccupant() {
        return occupant;
    }

    public boolean isFree() {
        return occupant == null;
    }

    public void seat(Customer customer) {
        this.occupant = customer;
    }

    public void release() {
        this.occupant = null;
    }
}
