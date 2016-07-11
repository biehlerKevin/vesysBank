package jmr.Requests;


import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import jmr.Request;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class WithdrawRequest extends Request {

    public String number;
    public double amount;

    public WithdrawRequest(String number, double amount){
        this.number = number;
        this.amount = amount;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            b.getAccount(number).withdraw(amount);
        } catch (IOException e) {
            this.setException(e);
        } catch (InactiveException e) {
            this.setException(e);
        } catch (OverdrawException e) {
            this.setException(e);
        }
    }
}
