package jmr.Requests;


import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import jmr.Request;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class TransferRequest extends Request {

    public String from;
    public String to;
    public double amount;

    public TransferRequest(String from, String to, double amount){
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void handleRequest(Bank b) {
        try {
            b.transfer(b.getAccount(from),b.getAccount(to),amount);
        } catch (IOException e) {
            this.setException(e);
        } catch (InactiveException e) {
            this.setException(e);
        } catch (OverdrawException e) {
            this.setException(e);
        } catch (IllegalArgumentException e) {
            this.setException(e);
        }
    }
}
