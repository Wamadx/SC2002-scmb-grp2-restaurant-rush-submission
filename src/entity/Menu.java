package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private List<MenuItem> items;

    public Menu() {
        this.items = new ArrayList<>();
        MenuItem salad = new MenuItem("Salad", 900, 1);
        MenuItem burger = new MenuItem("Burger", 1500, 1);
        MenuItem pasta = new MenuItem("Pasta", 1800, 2);
        this.items.add(salad);
        this.items.add(burger);
        this.items.add(pasta);
        this.items.add(new ComboMeal(salad, pasta));
    }

    public static Menu createStandardMenu() {
        return new Menu();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public MenuItem getItem(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
