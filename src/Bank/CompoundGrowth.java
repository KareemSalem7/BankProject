package Bank;

public interface CompoundGrowth {
    // compound rate for each type of account
    final static double savingsCompoundRate = 2.5;
    final static double businessCompoundRate = 0.5;

    // business accounts will receive $10 after every 3 months
    // as a loyalty bonus
    final static int bonusPeriod = 3;
    final static int bonus = 100;

    // increase the account balance after a month
    // abstraction:
    // this interface defines WHAT compound growth behavior accounts must have,
    // but not HOW it is implemented.
    //
    // SavingsAccount and BusinessAccount both implement this interface
    // differently with their own monthlyCompound() logic.
    void monthlyCompound();
}
