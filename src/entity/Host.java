package entity;

import control.InvalidActionException;

// The Host has its own task each turn: seat the customer who has waited longest, if a table is free.
public class Host extends Staff {
    public boolean seatFirstInQueue(WaitingQueue queue, Table table) throws InvalidActionException {
        ensureCanAct();
        if (queue.isEmpty()) {
            throw new InvalidActionException("No customer is waiting in the queue.");
        }
        if (table == null || !table.isFree()) {
            throw new InvalidActionException("No free table.");
        }
        queue.moveToTable(queue.getAll().get(0), table);
        useTask();
        return true;
    }

    @Override
    public String getRoleName() {
        return "Host";
    }
}
