package entity;

// Happy Hour: the manager can switch it on once per game, at the start of a turn.
// It lasts for that turn and the next. Orders taken while it is active get 20% off.
public class HappyHour {
    public static final int PRICE_PERCENT = 80;
    public static final int RECOVERY = 15;
    private static final int DURATION = 2;

    private boolean used = false;
    private int startTurn = -1;

    public boolean canActivate() {
        return !used;
    }

    public void activate(int turn) {
        this.used = true;
        this.startTurn = turn;
    }

    public boolean isActive(int turn) {
        return used && turn >= startTurn && turn < startTurn + DURATION;
    }

    // Number of active turns left, counting the current turn (0 when not active).
    public int remainingTurns(int turn) {
        return isActive(turn) ? startTurn + DURATION - turn : 0;
    }

    public int pricePercent(int turn) {
        return isActive(turn) ? PRICE_PERCENT : 100;
    }

    public boolean isUsed() {
        return used;
    }
}
