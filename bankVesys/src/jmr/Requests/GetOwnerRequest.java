package jmr.Requests;


import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import jmr.Request;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class GetOwnerRequest extends Request {

    public String number;
    public String owner;

    public GetOwnerRequest(String number){
        this.number = number;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            if(b.getAccount(number) != null)
                owner = b.getAccount(number).getOwner();
            else owner = null;
        } catch (IOException e) {
            this.setException(e);
        }
    }
}
