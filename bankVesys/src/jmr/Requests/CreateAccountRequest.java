package jmr.Requests;


import bank.Bank;
import jmr.Request;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class CreateAccountRequest extends Request {

    public String number;
    public String owner;

    public CreateAccountRequest(String owner){
        this.owner = owner;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            number = b.createAccount(owner);
        } catch (IOException e) {
            this.setException(e);
        }
    }
}
