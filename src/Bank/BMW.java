package Bank;

// BMW is just a BusinessAccount with its own rules:
//   - loyalty bonus every 2 months instead of 3
//   - credit expands by $500 every 3 months instead of $100 every 5
// All the actual withdraw/compound/credit logic is inherited from BusinessAccount.
public class BMW extends BusinessAccount {
    public BMW(String cardholderName, int creditScore) {
        super(cardholderName, creditScore, 2, 3, 500);
    }
}
