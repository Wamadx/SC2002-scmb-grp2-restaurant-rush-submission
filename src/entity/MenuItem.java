package entity;

public class MenuItem {
    private String name;
    private double price;
    private int prepUnits;

    public MenuItem(String name, double price, int prepUnits) {
        this.name = name;
        this.price = price;
        this.prepUnits = prepUnits;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getPrepUnits() {
        return prepUnits;
    }
}
