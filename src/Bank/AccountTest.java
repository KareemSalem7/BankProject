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