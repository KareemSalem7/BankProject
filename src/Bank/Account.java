package Bank;

import java.util.HashMap;
import java.util.Map;

public abstract class Account {
    // use this as public information between me and the cardholder
    public String cardholderName;

    // encapsulation:
    // balance is private, so outside classes cannot directly access or modify it.
    //
    // Instead, child classes like SavingsAccount and BusinessAccount
    // must interact with balance through controlled methods such as
    // getBalance(), increaseBalance(), decreaseBalance(), and setBalance().
    //
    // This protects the internal state of the Account class and prevents
    // unsafe direct modification of the balance.
    private double balance;

    // use this for identifying the person's account to do transfers for savings accounts
    // implemented as a dictionary (maps in java) and is common to all accounts
    // key = cardholder name
    // value = Account object
    public static Map<String, Account> accounts = new HashMap<>();

    // constructor
    public Account(String cardholderName){
        this.cardholderName = cardholderName;
        this.balance = 0;
        // we use "this" to refer to the SavingsAccount object being created
        // polymorphism:
        // reference type is Account in the map,
        // but object type is SavingsAccount or BusinessAccount when being put in
        Account.accounts.put(cardholderName, this);
    }

    public void deposit(double amount){
        this.balance += amount;
    }

    public void increaseBalance(double amount){
        this.balance += amount;
    }

    public void decreaseBalance(double amount){
        this.balance -= amount;
    }

    public void setBalance(double amount){
        this.balance = amount;
    }

    public double getBalance(){
        return this.balance;
    }

    // withdraw is abstract here and implemented differently
    // in different children classes, an example of abstraction
    abstract void withdraw(double amount);
}
