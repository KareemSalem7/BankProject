package Bank;

public class BusinessAccount extends Account implements CompoundGrowth, CreditExtension{
    // depending on your credit score, you get a certain amount of credit (debt you can go into)
    // credit = score * 100 (score between 1-5)
    public double credit;

    int numMonthsCompounded;
    // business accounts have num months compounded to decide loyalty bonus and credit increases

    public BusinessAccount(String cardholderName, int creditScore) {
        super(cardholderName);
        this.credit = creditScore * 100;
        this.numMonthsCompounded = 0;
    }

    // inheritance:
    // BusinessAccount extends Account, so it inherits common
    // attributes like balance and methods like deposit()
    // from the parent Account class
    @Override
    void withdraw(double amount) {
        double balance = this.getBalance();
        if (amount > balance + this.credit) {
            System.out.println("Insufficient Funds");
        } else {
            if (amount <= balance) {
                this.decreaseBalance(amount);
            } else {
                double remaining = amount - balance;

                this.setBalance(0);
                this.credit -= remaining;
            }

            balance = this.getBalance();
            System.out.println(
                    "Withdrew " + amount +
                            ". New Balance: " + balance +
                            ". Remaining Credit: " + this.credit
            );
        }
    }

    @Override
    public void monthlyCompound() {
        double balance = this.getBalance();
        // increase the balance by the interest rate
        this.increaseBalance(balance * CompoundGrowth.businessCompoundRate);

        // check if the loyalty bonus will be applied
        this.numMonthsCompounded += 1;
        if (this.numMonthsCompounded >= CompoundGrowth.bonusPeriod){
            this.increaseBalance(CompoundGrowth.bonus);
            this.numMonthsCompounded = 0;
            System.out.println("Congratulations! Your loyalty has earned you $" + CompoundGrowth.bonus + "!");
        }
    }

    @Override
    public void expandCredit() {
        if (this.numMonthsCompounded >= CreditExtension.monthsRequired){
            this.credit += CreditExtension.creditIncrease;
            System.out.println("Congratulations! Your credit is now $" + this.credit + "!");
        } else {
            System.out.println("You still need to wait " + (CreditExtension.monthsRequired - this.numMonthsCompounded) + "months before a credit expansion.");
        }
    }
}
