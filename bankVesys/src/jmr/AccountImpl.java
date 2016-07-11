package jmr;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public class AccountImpl implements Account {

    private final Account acc;
    private final UpdateHandler handler;

    public AccountImpl(Account acc, UpdateHandler handler) {
        this.acc = acc;
        this.handler = handler;
    }

    @Override
    public String getNumber() throws IOException {
        return acc.getNumber();
    }

    @Override
    public String getOwner() throws IOException {
        return acc.getOwner();
    }

    @Override
    public boolean isActive() throws IOException {
        return acc.isActive();
    }

    @Override
    public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
        acc.deposit(amount);
        handler.accountChanged(acc.getNumber());
    }

    @Override
    public void withdraw(double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        acc.withdraw(amount);
        handler.accountChanged(acc.getNumber());
    }

    @Override
    public double getBalance() throws IOException {
        return acc.getBalance();
    }
}
