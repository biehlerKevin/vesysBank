package jmr.Requests;


import bank.Bank;
import bank.InactiveException;
import jmr.Request;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class DepositRequest extends Request {

    public String number;
    public double amount;

    public DepositRequest(String number, double amount){
        this.number = number;
        this.amount = amount;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            b.getAccount(number).deposit(amount);
        } catch (IOException e) {
            this.setException(e);
        } catch (InactiveException e) {
            this.setException(e);
        }
    }
}
