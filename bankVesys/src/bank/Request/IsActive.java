package bank.Request;

/**
 * Created by matz on 07.03.2016.
 */
public class IsActive implements Request {
    public final String accountNumber;

    public IsActive(String accountNumber){
        this.accountNumber = accountNumber;
    }
}
