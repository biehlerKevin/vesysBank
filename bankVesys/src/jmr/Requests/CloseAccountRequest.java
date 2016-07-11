package jmr.Requests;


import bank.Bank;
import bank.Request.CloseAccount;
import jmr.Request;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mfrey on 11/07/2016.
 */
public class CloseAccountRequest extends Request {

    public boolean success = false;
    public String number;

    public CloseAccountRequest(String number){
        this.number = number;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            success = b.closeAccount(number);
        } catch (IOException e) {
            this.setException(e);
        }
    }
}
