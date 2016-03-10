package bank.Request;

/**
 * Created by matz on 07.03.2016.
 */
public class GetOwner implements Request {
    public final String accountNumber;

    public GetOwner(String accountNumber){
        this.accountNumber = accountNumber;
    }
}
