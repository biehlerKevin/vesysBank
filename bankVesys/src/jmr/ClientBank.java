package jmr;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import jmr.Requests.*;

import java.io.IOException;
import java.util.Set;

public class ClientBank implements Bank {

    private final RequestHandler jmsHandler;

    public ClientBank(RequestHandler jmsHandler) {
        this.jmsHandler = jmsHandler;
    }

    @Override
    public String createAccount(String owner) throws IOException {
        CreateAccountRequest res = (CreateAccountRequest) jmsHandler.handle(new CreateAccountRequest(owner));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
        }
        return res.number;
    }

    @Override
    public boolean closeAccount(String number) throws IOException {
        CloseAccountRequest res = (CloseAccountRequest) jmsHandler.handle(new CloseAccountRequest(number));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
        }
        return res.success;
    }

    @Override
    public Set<String> getAccountNumbers() throws IOException {
        GetAccountNumbersRequest res = (GetAccountNumbersRequest) jmsHandler.handle(new GetAccountNumbersRequest());
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
        }
        return res.numbers;
    }

    @Override
    public Account getAccount(String number) throws IOException {
        GetOwnerRequest res = (GetOwnerRequest) jmsHandler.handle(new GetOwnerRequest(number));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
        }
        if(res.owner == null) return null;
        return new ClientAccount(number, res.owner, jmsHandler);
    }

    @Override
    public void transfer(Account a, Account b, double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        TransferRequest res = (TransferRequest) jmsHandler.handle(new TransferRequest(a.getNumber(),b.getNumber(),amount));
        Exception ex = res.getException();
        if(ex != null){
            if(ex instanceof IOException)
                throw (IOException) ex;
            else if(ex instanceof IllegalArgumentException)
                throw (IllegalArgumentException) ex;
            else if(ex instanceof OverdrawException)
                throw (OverdrawException) ex;
            else if(ex instanceof InactiveException)
                throw (InactiveException) ex;
        }
    }
}
