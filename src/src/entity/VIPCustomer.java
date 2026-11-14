package entity;

// VIP -5 every turn and 10per off
public class VIPCustomer extends Customer {
    private static final int VIP_WAITING_LOSS = 5;
    private static final int VIP_PRICE_PERCENT = 90;

    public VIPCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public String getTypeName() {
        return "VIP";
    }

    @Override
    public int getWaitingLoss() {
        return VIP_WAITING_LOSS;
    }

    @Override
    public int calculateBill(MenuItem item) {
        return Money.percentOf(super.calculateBill(item), VIP_PRICE_PERCENT);
    }
}
