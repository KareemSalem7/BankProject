package Bank;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // polymorphism:
        // reference type is Account
        // object types are different child classes
        ArrayList<Account> accounts = new ArrayList<>();

        Account savings1 = new SavingsAccount("Homelander");
        Account business1 = new BusinessAccount("Soldier Boy", 5);

        accounts.add(savings1);
        accounts.add(business1);

        // deposits
        savings1.deposit(500);
        business1.deposit(300);

        // withdrawals
        savings1.withdraw(200);

        System.out.println();

        business1.withdraw(700);

        System.out.println();

        business1.withdraw(200);

        System.out.println();

        // polymorphism test
        // Java decides at runtime which withdraw method to use
        for (Account acc : accounts) {

            System.out.println("Current balance: " + acc.getBalance());

            acc.withdraw(50);

            System.out.println();
        }
    }
}