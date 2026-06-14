package Bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @BeforeEach
    void resetAccounts() {
        Account.accounts.clear();
    }

    @Test
    void depositIncreasesBalance() {
        Account savings = new SavingsAccount("Homelander");

        savings.deposit(500);

        assertEquals(500, savings.getBalance());
    }

    @Test
    void savingsWithdrawDecreasesBalance() {
        Account savings = new SavingsAccount("Homelander");
        savings.deposit(500);

        savings.withdraw(200);

        assertEquals(300, savings.getBalance());
    }

    @Test
    void savingsCannotWithdrawMoreThanBalance() {
        Account savings = new SavingsAccount("Homelander");
        savings.deposit(100);

        savings.withdraw(200);

        assertEquals(100, savings.getBalance());
    }

    @Test
    void businessWithdrawUsesBalanceFirst() {
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5);
        business.deposit(300);

        business.withdraw(200);

        assertEquals(100, business.getBalance());
        assertEquals(500, business.credit);
    }

    @Test
    void businessWithdrawUsesCreditWhenBalanceRunsOut() {
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5);
        business.deposit(300);

        business.withdraw(700);

        assertEquals(0, business.getBalance());
        assertEquals(100, business.credit);
    }

    @Test
    void businessCannotWithdrawMoreThanBalanceAndCredit() {
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5);
        business.deposit(300);

        business.withdraw(900);

        assertEquals(300, business.getBalance());
        assertEquals(500, business.credit);
    }

    @Test
    void savingsMonthlyCompoundIncreasesBalance() {
        SavingsAccount savings = new SavingsAccount("Homelander");
        savings.deposit(100);

        savings.monthlyCompound();

        assertEquals(350, savings.getBalance());
    }

    @Test
    void businessMonthlyCompoundIncreasesBalance() {
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5);
        business.deposit(100);

        business.monthlyCompound();

        assertEquals(150, business.getBalance());
    }

    @Test
    void businessGetsLoyaltyBonusAfterThreeMonths() {
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5);
        business.deposit(100);

        business.monthlyCompound();
        business.monthlyCompound();
        business.monthlyCompound();

        assertEquals(437.5, business.getBalance());
    }

    @Test
    void regularBusinessAccountUsesDefaultRules() {
        // a non-BMW business account keeps the shared interface defaults:
        // bonus every 3 months, credit +$100 every 5 months
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5);

        assertEquals(3, business.bonusPeriod);
        assertEquals(5, business.monthsRequired);
        assertEquals(100, business.creditIncrease);
    }

    @Test
    void regularBusinessExpandsCreditByOneHundredAfterFiveMonths() {
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5); // credit = 500

        // compound 5 real months: the 3-month loyalty bonus fires along the way
        // but must NOT reset the counter, otherwise credit expansion never reaches 5
        for (int i = 0; i < 5; i++) {
            business.monthlyCompound();
        }

        business.expandCredit();

        assertEquals(600, business.credit); // +$100 default increase
    }

    @Test
    void loyaltyBonusAndCreditExpansionRunOnIndependentCycles() {
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5);

        // 3 months -> loyalty bonus cycle hits, but credit must not expand yet
        for (int i = 0; i < 3; i++) {
            business.monthlyCompound();
        }
        business.expandCredit();
        assertEquals(500, business.credit); // unchanged at month 3

        // months 4 and 5 -> now the 5-month credit cycle hits
        business.monthlyCompound();
        business.monthlyCompound();
        business.expandCredit();
        assertEquals(600, business.credit);
    }

    @Test
    void creditExpansionRepeatsEachCycle() {
        // regression guard: the running counter must keep matching the 5-month
        // cycle, so credit expands again at month 10, 15, ... not just the first time
        BusinessAccount business = new BusinessAccount("Soldier Boy", 5); // credit = 500

        for (int i = 0; i < 5; i++) business.monthlyCompound();
        business.expandCredit();
        assertEquals(600, business.credit); // month 5

        for (int i = 0; i < 5; i++) business.monthlyCompound();
        business.expandCredit();
        assertEquals(700, business.credit); // month 10
    }

    @Test
    void bmwExpandsCreditByFiveHundredAfterThreeMonths() {
        // BMW overrides the credit rules: +$500 every 3 months instead of +$100 every 5
        BMW bmw = new BMW("Stormfront", 5); // credit = 500

        for (int i = 0; i < 3; i++) bmw.monthlyCompound();
        bmw.expandCredit();

        assertEquals(1000, bmw.credit); // 500 + 500
    }

    @Test
    void bmwGetsLoyaltyBonusAfterTwoMonths() {
        // BMW's loyalty bonus period is 2 months instead of the default 3
        BMW bmw = new BMW("Stormfront", 5);
        bmw.deposit(100);

        bmw.monthlyCompound(); // 100 + 50 = 150, monthsCompounded = 1
        bmw.monthlyCompound(); // 150 + 75 = 225, then +100 bonus = 325

        assertEquals(325, bmw.getBalance());
    }

    @Test
    void bmwReusesBusinessWithdrawLogic() {
        // BMW inherits the credit-backed withdraw from BusinessAccount
        BMW bmw = new BMW("Stormfront", 5);
        bmw.deposit(300);

        bmw.withdraw(700); // 300 from balance, 400 from the 500 credit

        assertEquals(0, bmw.getBalance());
        assertEquals(100, bmw.credit);
    }

    @Test
    void savingsCanTransferToAnotherSavingsAccount() {
        SavingsAccount sender = new SavingsAccount("Homelander");
        SavingsAccount receiver = new SavingsAccount("Butcher");

        sender.deposit(500);

        sender.transfer(200, "Butcher");

        assertEquals(300, sender.getBalance());
        assertEquals(200, receiver.getBalance());
    }

    @Test
    void savingsCannotTransferMoreThanBalance() {
        SavingsAccount sender = new SavingsAccount("Homelander");
        SavingsAccount receiver = new SavingsAccount("Butcher");

        sender.deposit(100);

        sender.transfer(200, "Butcher");

        assertEquals(100, sender.getBalance());
        assertEquals(0, receiver.getBalance());
    }

    @Test
    void accountMapStoresDifferentAccountTypesPolymorphically() {
        Account savings = new SavingsAccount("Homelander");
        Account business = new BusinessAccount("Soldier Boy", 5);

        assertTrue(Account.accounts.get("Homelander") instanceof SavingsAccount);
        assertTrue(Account.accounts.get("Soldier Boy") instanceof BusinessAccount);
    }

    @Test
    void compoundGrowthInterfaceWorksPolymorphically() {
        CompoundGrowth savings = new SavingsAccount("Homelander");
        CompoundGrowth business = new BusinessAccount("Soldier Boy", 5);

        ((Account) savings).deposit(100);
        ((Account) business).deposit(100);

        savings.monthlyCompound();
        business.monthlyCompound();

        assertEquals(350, ((Account) savings).getBalance());
        assertEquals(150, ((Account) business).getBalance());
    }
}