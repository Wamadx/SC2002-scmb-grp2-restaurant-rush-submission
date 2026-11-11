package entity;

import control.InvalidActionException;

public abstract class Staff {
    private boolean taskUsed = false;

    public void resetForTurn() {
        this.taskUsed = false;
    }

    public boolean canAct() {
        return !this.taskUsed;
    }

    public void useTask() {
        this.taskUsed = true;
    }

    protected void ensureCanAct() throws InvalidActionException {
        if (!canAct()) {
            throw new InvalidActionException(getRoleName() + " has already acted this turn.");
        }
    }

    public abstract String getRoleName();
}
