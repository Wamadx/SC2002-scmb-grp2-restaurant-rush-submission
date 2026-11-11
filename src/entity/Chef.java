package entity;

import control.InvalidActionException;

// The chef advances one order by one step each turn.
public class Chef extends Staff {
    public boolean prepare(Order order, int turn) throws InvalidActionException {
        ensureCanAct();
        if (order == null || !order.needsPreparation()) {
            throw new InvalidActionException("Order does not need preparation.");
        }
        order.advance(turn);
        useTask();
        return true;
    }

    @Override
    public String getRoleName() {
        return "Chef";
    }
}
