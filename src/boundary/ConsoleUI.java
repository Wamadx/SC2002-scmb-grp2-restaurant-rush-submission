package boundary;

import control.GameController;
import entity.*;
import enums.GameResult;
import java.util.List;
import java.util.Scanner;

// All screen output and menu input.
public class ConsoleUI {
    private static final String LINE = "==============================================================";
    private static final String THIN = "--------------------------------------------------------------";

    private final Scanner scanner = new Scanner(System.in);
    private final boolean echoInput;

    public ConsoleUI() {
        this(false);
    }

    public ConsoleUI(boolean echoInput) {
        this.echoInput = echoInput;
    }

    public int readInt(String prompt) {
        return readInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("no more input.");
            }
            String line = scanner.nextLine().trim();
            if (echoInput) {
                System.out.println(line);
            }
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }

    public int readChoice(String title, List<String> options, String zeroLabel) {
        System.out.println(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + options.get(i));
        }
        System.out.println("  0. " + zeroLabel);
        return readInt("> ", 0, options.size());
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayWelcome() {
        System.out.println(LINE);
        System.out.println(" RESTAURANT RUSH - Stage 3");
        System.out.println(LINE);
        System.out.println(" You are the manager. 18 turns, 2 tables, 1 Waiter, 1 Host, 1 Chef, 1 Cashier.");
        System.out.println(" Customers arrive on turns 1, 4, 7, 10, 13 and 16 (turn 4 is a VIP, turn 10 a Critic).");
        System.out.println(" Menu: Salad $9.00 (1 unit), Burger $15.00 (1 unit), Pasta $18.00 (2 units),");
        System.out.println("       Salad + Pasta Combo $24.30 (3 units). Happy Hour can be switched on once per game.");
        System.out.println(" Win: at least " + GameController.TARGET_PAID_CUSTOMERS + " paid customers, revenue at least "
                + formatMoney(GameController.TARGET_REVENUE) + ", average satisfaction at least "
                + GameController.TARGET_AVERAGE_SATISFACTION + ".");
    }

    public void displayTurnHeader(int turn) {
        System.out.println();
        System.out.println(LINE);
        System.out.println(" TURN " + turn + " / " + GameController.MAX_TURNS);
        System.out.println(LINE);
    }

    public void displayStartOfTurn(int turn, Restaurant restaurant) {
        System.out.println(" Revenue: " + formatMoney(restaurant.getRevenue())
                + "    Avg satisfaction (paid): " + formatAverage(restaurant.getAverageSatisfaction()));

        StringBuilder queue = new StringBuilder();
        for (Customer c : restaurant.getWaitingQueue().getAll()) {
            queue.append(c.getLabel()).append(" (sat ").append(c.getSatisfaction()).append(")  ");
        }
        System.out.println(" Waiting queue: " + (queue.length() == 0 ? "(empty)" : queue.toString().trim()));
        System.out.println(" Happy Hour: " + describeHappyHour(restaurant.getHappyHour(), turn));

        for (Table t : restaurant.getTables()) {
            if (t.isFree()) {
                System.out.println(" Table " + t.getId() + ": (free)");
            } else {
                System.out.println(" Table " + t.getId() + ": " + describeCustomer(t.getOccupant()));
            }
        }

        System.out.println(" Staff tasks available:");
        int waiting = restaurant.getWaitingQueue().getAll().size();
        boolean freeTable = restaurant.getFreeTable() != null;
        System.out.println("   Waiter : seat (" + waiting + " waiting, " + (freeTable ? "table free" : "no free table")
                + ") | take order (" + restaurant.getCustomersWaitingToOrder().size()
                + ") | serve (" + restaurant.getReadyOrders().size() + " READY)");
        System.out.println("   Host   : seat longest-waiting customer automatically (" + waiting + " waiting, " + (freeTable ? "table free" : "no free table") + ")");
        System.out.println("   Chef   : orders to prepare (" + restaurant.getOrdersNeedingPreparation().size() + ")");
        System.out.println("   Cashier: automatic, " + restaurant.getReadyToPayCount() + " customer(s) READY_TO_PAY");
        System.out.println(THIN);
    }

    public void displayEndOfTurn(int turn, Restaurant restaurant) {
        System.out.println(THIN);
        System.out.println(" End of turn " + turn
                + " | served: " + restaurant.getServedCount()
                + " | unhappy departures: " + restaurant.getAbandonedCount()
                + " | revenue: " + formatMoney(restaurant.getRevenue())
                + " | avg satisfaction: " + formatAverage(restaurant.getAverageSatisfaction())
                + " | turns used: " + turn + "/" + GameController.MAX_TURNS);
    }

    public void displayFinalResult(GameResult result, Restaurant restaurant) {
        System.out.println();
        System.out.println(LINE);
        System.out.println(" GAME OVER - " + result);
        System.out.println(LINE);
        System.out.println(" Paid customers    : " + restaurant.getPaidCount()
                + " (target " + GameController.TARGET_PAID_CUSTOMERS + ")");
        System.out.println(" Revenue           : " + formatMoney(restaurant.getRevenue())
                + " (target " + formatMoney(GameController.TARGET_REVENUE) + ")");
        System.out.println(" Avg satisfaction  : " + formatAverage(restaurant.getAverageSatisfaction())
                + " (target " + GameController.TARGET_AVERAGE_SATISFACTION + ")");
        System.out.println(" Unhappy departures: " + restaurant.getAbandonedCount());
        System.out.println(result == GameResult.VICTORY
                ? " Congratulations, you met all three targets!"
                : " Not all targets were met. Better luck next time.");
    }

    private String describeHappyHour(HappyHour happyHour, int turn) {
        if (happyHour.isActive(turn)) {
            return "ACTIVE, " + happyHour.remainingTurns(turn) + " turn(s) left including this one (20% off new orders)";
        }
        return happyHour.canActivate() ? "available (not used yet)" : "used, not active";
    }

    private String describeCustomer(Customer c) {
        String text = c.getLabel() + "  sat " + c.getSatisfaction() + "  " + c.getStatus();
        Order o = c.getOrder();
        if (o != null) {
            text += "  | Order #" + o.getId() + " " + o.getItem().getName() + " "
                    + o.getProgressUnits() + "/" + o.getItem().getPrepUnits() + " " + o.getStatus();
        }
        return text;
    }

    // Amounts are kept in cents and shown as dollars with two decimals.
    public static String formatMoney(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }

    private static String formatAverage(double average) {
        return average < 0 ? "N/A" : String.format("%.1f", average);
    }
}
