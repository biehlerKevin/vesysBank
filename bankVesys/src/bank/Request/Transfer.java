package bank.Request;

/**
 * Created by matz on 07.03.2016.
 */
public class Transfer implements Request {
    public final String fromAccountNumber;
    public final String toAccountNumber;
    public final double amount;

    public Transfer(String from, String to, double amount){
        this.fromAccountNumber = from;
        this.toAccountNumber = to;
        this.amount = amount;
    }
}
