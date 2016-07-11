package jmr.Requests;


import bank.Bank;
import jmr.Request;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class GetBalanceRequest extends Request {

    public String number;
    public double balance;

    public GetBalanceRequest(String number){
        this.number = number;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            balance = b.getAccount(number).getBalance();
        } catch (IOException e) {
            this.setException(e);
        }
    }
}
