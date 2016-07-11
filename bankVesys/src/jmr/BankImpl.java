package jmr;


import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mfrey on 11/07/2016.
 */
public class BankImpl implements Bank {
    private final Bank bank;
    private final UpdateHandler handler;

    public BankImpl(Bank bank, UpdateHandler handler){
        this.bank = bank;
        this.handler = handler;
    }

    @Override
    public String createAccount(String owner) throws IOException {
        String id = bank.createAccount(owner);
        if(id != null) handler.accountChanged(id);
        return id;
    }

    @Override
    public boolean closeAccount(String number) throws IOException {
        boolean res = bank.closeAccount(number);
        if(res) handler.accountChanged(number);
        return res;
    }

    @Override
    public Set<String> getAccountNumbers() throws IOException {
        return new HashSet<>(bank.getAccountNumbers());
    }

    @Override
    public Account getAccount(String number) throws IOException {
        Account acc = bank.getAccount(number);
        return acc == null ? null : new AccountImpl(acc, handler);
    }

    @Override
    public void transfer(Account a, Account b, double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        bank.transfer(a,b,amount);
        handler.accountChanged(a.getNumber());
        handler.accountChanged(b.getNumber());
    }
}
