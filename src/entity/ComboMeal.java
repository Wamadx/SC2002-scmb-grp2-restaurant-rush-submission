package entity;

// A combo is made of two existing menu items.
// Price is 90% of the components' combined price, rounded to the cent. Preparation units are summed.
public class ComboMeal extends MenuItem {
    private static final int COMBO_PRICE_PERCENT = 90;

    public ComboMeal(MenuItem first, MenuItem second) {
        super(first.getName() + " + " + second.getName() + " Combo",
                Money.percentOf(first.getPrice() + second.getPrice(), COMBO_PRICE_PERCENT),
                first.getPrepUnits() + second.getPrepUnits());
    }
}
