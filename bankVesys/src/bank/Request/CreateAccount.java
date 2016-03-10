package bank.Request;

/**
 * Created by matz on 07.03.2016.
 */
public class CreateAccount implements Request {
    public final String owner;

    public CreateAccount(String owner){
        this.owner = owner;
    }
}
