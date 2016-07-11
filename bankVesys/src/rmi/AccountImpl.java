package rmi;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AccountImpl extends UnicastRemoteObject implements AccountRemote{

    private final Account acc;
    private final BankImpl bank;

    AccountImpl(Account a, BankImpl bank) throws RemoteException {
        this.acc = a;
        this.bank = bank;
    }

    @Override
    public double getBalance() throws IOException {
        return acc.getBalance();
    }

    @Override
    public String getOwner() throws IOException {
        return acc.getOwner();
    }

    @Override
    public String getNumber() throws IOException {
        return acc.getNumber();
    }

    @Override
    public boolean isActive() throws IOException {
        return acc.isActive();
    }

    @Override
    public void deposit(double amount) throws IOException, InactiveException {
        acc.deposit(amount);
        bank.notifyListeners(acc.getNumber());
    }

    @Override
    public void withdraw(double amount) throws IOException, InactiveException, OverdrawException {
        acc.withdraw(amount);
        bank.notifyListeners(acc.getNumber());
    }
}
