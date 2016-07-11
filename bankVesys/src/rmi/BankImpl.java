package rmi;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class BankImpl extends UnicastRemoteObject implements BankRemote {

    private final Bank bank;

    private List<RemoteUpdateHandler> listeners = new CopyOnWriteArrayList<>();

    public BankImpl(Bank bank) throws RemoteException{
        this.bank = bank;
    }

    @Override
    public String createAccount(String owner) throws IOException {
        String id = bank.createAccount(owner);
        if(id != null) notifyListeners(id);
        return id;
    }

    @Override
    public boolean closeAccount(String number) throws IOException {
        boolean res = bank.closeAccount(number);
        if(res) notifyListeners(number);
        return res;
    }

    @Override
    public Set<String> getAccountNumbers() throws IOException {
        return new HashSet<>(bank.getAccountNumbers());
    }

    @Override
    public Account getAccount(String number) throws IOException {
        Account a = bank.getAccount(number);
        return a==null ? null : new AccountImpl(a, this);
    }

    @Override
    public void transfer(Account a, Account b, double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        bank.transfer(a, b, amount);
    }

    void notifyListeners(String id) throws IOException{
        for(RemoteUpdateHandler h:listeners){
            h.accountChanged(id);
        }
    }

    @Override
    public void registerUpdateHandler(RemoteUpdateHandler handler) throws RemoteException {
        listeners.add(handler);
    }
}
