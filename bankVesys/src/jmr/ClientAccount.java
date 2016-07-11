package jmr;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;
import jmr.Requests.*;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class ClientAccount implements Account {

    private String number;
    private String owner;
    private RequestHandler jmsHandler;

    public ClientAccount(String number, String owner, RequestHandler jmsHandler){
        this.number = number;
        this.owner = owner;
        this.jmsHandler = jmsHandler;
    }

    @Override
    public String getNumber() throws IOException {
        return number;
    }

    @Override
    public String getOwner() throws IOException {
        return owner;
    }

    @Override
    public boolean isActive() throws IOException {
        IsActiveRequest res = (IsActiveRequest) jmsHandler.handle(new IsActiveRequest(number));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
        }
        return res.isActive;
    }

    @Override
    public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
        DepositRequest res = (DepositRequest) jmsHandler.handle(new DepositRequest(number, amount));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
            else if(ex instanceof IllegalArgumentException)
                throw (IllegalArgumentException) ex;
            else if(ex instanceof InactiveException)
                throw (InactiveException) ex;
        }
    }

    @Override
    public void withdraw(double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        WithdrawRequest res = (WithdrawRequest) jmsHandler.handle(new WithdrawRequest(number, amount));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
            else if(ex instanceof IllegalArgumentException)
                throw (IllegalArgumentException) ex;
            else if(ex instanceof InactiveException)
                throw (InactiveException) ex;
            else if(ex instanceof OverdrawException)
                throw (OverdrawException) ex;
        }
    }

    @Override
    public double getBalance() throws IOException {
        GetBalanceRequest res = (GetBalanceRequest) jmsHandler.handle(new GetBalanceRequest(number));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
        }
        return res.balance;
    }
}
