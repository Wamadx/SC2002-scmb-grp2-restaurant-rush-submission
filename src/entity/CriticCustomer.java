package entity;

// Critic -12 always pasta
public class CriticCustomer extends Customer {
    private static final int CRITIC_WAITING_LOSS = 12;

    public CriticCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public String getTypeName() {
        return "Critic";
    }

    @Override
    public int getWaitingLoss() {
        return CRITIC_WAITING_LOSS;
    }

    @Override
    public MenuItem chooseFood(Menu menu) {
        return menu.getItem("Pasta");
    }
}
