package Bank;

public class SavingsAccount extends Account implements CompoundGrowth, SavingsTransfer{

    public SavingsAccount(String cardholderName) {
        super(cardholderName);
    }

    @Override
    void withdraw(double amount) {
        double balance = this.getBalance();
        // for a savings account, you cannot withdraw more than you have
        if (amount > balance){
            System.out.println("Insufficient Funds");
        }
        else {
            this.decreaseBalance(amount);
            balance = this.getBalance();
            System.out.println("Withdrew " + amount + ".New Balance: " + balance);
        }
    }

    @Override
    public void monthlyCompound() {
        double balance = this.getBalance();
        // increase the amount by the interest rate
        this.increaseBalance(balance * CompoundGrowth.savingsCompoundRate);
    }

    @Override
    public void transfer(double amount, String receiverName) {
        double balance = this.getBalance();
        // you cannot transfer more than you have
        if (amount > balance){
            System.out.println("Insufficient Funds");
        }
        else {
            this.decreaseBalance(amount);
            SavingsAccount recieverAccount = (SavingsAccount) SavingsAccount.accounts.get(receiverName);
            recieverAccount.deposit(amount);
            System.out.println("Transferred $" + amount + "to " + receiverName);
        }
    }
}
