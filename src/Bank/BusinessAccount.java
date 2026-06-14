package Bank;

public class BusinessAccount extends Account implements CompoundGrowth, CreditExtension{
    // depending on your credit score, you get a certain amount of credit (debt you can go into)
    // credit = score * 100 (score between 1-5)
    public double credit;

    int numMonthsCompounded;
    // business accounts have num months compounded to decide loyalty bonus and credit increases

    // these rules can vary per business account "type" (e.g. BMW), so we keep them
    // as instance fields instead of always using the shared interface constants
    int bonusPeriod;
    int monthsRequired;
    double creditIncrease;

    public BusinessAccount(String cardholderName, int creditScore) {
        // default business account uses the shared interface constants
        this(cardholderName, creditScore,
                CompoundGrowth.bonusPeriod, CreditExtension.monthsRequired, CreditExtension.creditIncrease);
    }

    public BusinessAccount(String cardholderName, int creditScore,
                           int bonusPeriod, int monthsRequired, double creditIncrease) {
        super(cardholderName);
        this.credit = creditScore * 100;
        this.numMonthsCompounded = 0;
        this.bonusPeriod = bonusPeriod;
        this.monthsRequired = monthsRequired;
        this.creditIncrease = creditIncrease;
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

        // check if the loyalty bonus will be applied.
        // numMonthsCompounded is a running total that is never reset, so the
        // loyalty bonus (every bonusPeriod months) and credit expansion (every
        // monthsRequired months) can each fire on their own independent cycle.
        this.numMonthsCompounded += 1;
        if (this.numMonthsCompounded % this.bonusPeriod == 0){
            this.increaseBalance(CompoundGrowth.bonus);
            System.out.println("Congratulations! Your loyalty has earned you $" + CompoundGrowth.bonus + "!");
        }
    }

    @Override
    public void expandCredit() {
        if (this.numMonthsCompounded > 0 && this.numMonthsCompounded % this.monthsRequired == 0){
            this.credit += this.creditIncrease;
            System.out.println("Congratulations! Your credit is now $" + this.credit + "!");
        } else {
            int monthsUntilNext = this.monthsRequired - (this.numMonthsCompounded % this.monthsRequired);
            System.out.println("You still need to wait " + monthsUntilNext + " months before a credit expansion.");
        }
    }
}
