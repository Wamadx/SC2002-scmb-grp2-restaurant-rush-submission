package entity;

public class MenuItem {
    private String name;
    private int priceCents;
    private int prepUnits;

    public MenuItem(String name, int priceCents, int prepUnits) {
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    public String getName() {
        return name;
    }

    // Price in cents.
    public int getPrice() {
        return priceCents;
    }

    public int getPrepUnits() {
        return prepUnits;
    }
}
