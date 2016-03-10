package bank.Request;

import javax.print.attribute.standard.RequestingUserName;

/**
 * Created by matz on 07.03.2016.
 */
public class GetAccount implements Request {
    public final String accountNumber;

    public GetAccount(String accountNumber){
        this.accountNumber = accountNumber;
    }
}
