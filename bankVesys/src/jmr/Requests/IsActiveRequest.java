package jmr.Requests;


import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import jmr.Request;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class IsActiveRequest extends Request {

    public String number;
    public boolean isActive;

    public IsActiveRequest(String number){
        this.number = number;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            isActive = b.getAccount(number).isActive();
        } catch (IOException e) {
            this.setException(e);
        }
    }
}
