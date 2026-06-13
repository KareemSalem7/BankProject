package Bank;

public interface CreditExtension {
    // number of months required to increase your credit
    // you can even customize this to be different for different banks
    static final int monthsRequired = 5;
    static final double creditIncrease = 100;

    void expandCredit();
}
