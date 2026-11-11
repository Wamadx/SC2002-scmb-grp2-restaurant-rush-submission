import boundary.ConsoleUI;
import control.GameController;

public class Main {
    public static void main(String[] args) {
        boolean replay = args.length > 0 && args[0].equals("--replay");
        ConsoleUI ui = new ConsoleUI(replay);
        try {
            new GameController(ui).runGame();
        } catch (IllegalStateException e) {
            System.out.println();
            System.out.println("Game stopped: " + e.getMessage());
        }
    }
}
