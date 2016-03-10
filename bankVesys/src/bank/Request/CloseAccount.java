package bank.Request;

/**
 * Created by matz on 07.03.2016.
 */
public class CloseAccount implements Request {
    public final String accountNumber;

    public CloseAccount(String accountNumber){
        this.accountNumber = accountNumber;
    }
}
