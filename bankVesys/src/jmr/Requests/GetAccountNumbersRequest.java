package jmr.Requests;


import bank.Bank;
import jmr.Request;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mfrey on 11/07/2016.
 */
public class GetAccountNumbersRequest extends Request {

    public Set<String> numbers;

    @Override
    public void handleRequest(Bank b) {
        try {
            numbers = b.getAccountNumbers();
        } catch (IOException e) {
            this.setException(e);
        }
    }
}
