package control;

import boundary.ConsoleUI;
import entity.Menu;
import entity.Restaurant;
import enums.GameResult;

// Starts the game, runs all 18 turns, then checks whether the player won or lost.
public class GameController {
    public static final int MAX_TURNS = 18;
    public static final int TABLE_COUNT = 2;
    public static final int TARGET_PAID_CUSTOMERS = 4;
    public static final int TARGET_REVENUE = 4500;
    public static final int TARGET_AVERAGE_SATISFACTION = 60;

    private ConsoleUI ui;
    private Restaurant restaurant;
    private TurnController turnController;
    private int currentTurn;

    public GameController() {
        this(new ConsoleUI(false));
    }

    public GameController(ConsoleUI ui) {
        this.ui = ui;
        initializeGame();
    }

    public void runGame() {
        ui.displayWelcome();
        for (currentTurn = 1; currentTurn <= MAX_TURNS; currentTurn++) {
            turnController.executeTurn(currentTurn);
        }
        ui.displayFinalResult(checkResult(), restaurant);
    }

    private void initializeGame() {
        this.currentTurn = 1;
        this.restaurant = new Restaurant("Restaurant Rush", TABLE_COUNT, Menu.createStandardMenu());
        this.turnController = new TurnController(restaurant, ui);
    }

    private GameResult checkResult() {
        boolean enoughPaid = restaurant.getPaidCount() >= TARGET_PAID_CUSTOMERS;
            boolean enoughRevenue = restaurant.getRevenue() >= TARGET_REVENUE;
            double average = restaurant.getAverageSatisfaction();
        boolean happyEnough = average >= TARGET_AVERAGE_SATISFACTION;
            return (enoughPaid && enoughRevenue && happyEnough) ? GameResult.VICTORY : GameResult.DEFEAT;
    }
}
