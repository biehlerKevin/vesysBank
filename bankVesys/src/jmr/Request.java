package jmr;

import bank.Bank;

import java.io.IOException;
import java.io.Serializable;

public abstract class Request implements Serializable {
    private Exception e;
    public void setException(Exception e){
        this.e = e;
    }
    public Exception getException(){
        return e;
    }
    public abstract void handleRequest(Bank b);
}
