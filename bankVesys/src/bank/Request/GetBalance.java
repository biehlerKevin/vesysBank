package bank.Request;

/**
 * Created by matz on 07.03.2016.
 */
public class GetBalance implements Request {
    public final String accountNumber;

    public GetBalance(String accountNumber){
        this.accountNumber = accountNumber;
    }
}
