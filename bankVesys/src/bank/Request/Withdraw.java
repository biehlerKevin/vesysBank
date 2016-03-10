package bank.Request;

/**
 * Created by matz on 07.03.2016.
 */
public class Withdraw implements Request {
    public final String accountNumber;
    public final double amount;

    public Withdraw(String accountNumber, double amount){
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
}
