package entity;

// money in cent for discount
public final class Money {
    private Money() {
    }

    // Returns the given percentage of an amount, rounded to the nearest cent.
    public static int percentOf(int cents, int percent) {
        return (cents * percent + 50) / 100;
    }
}
