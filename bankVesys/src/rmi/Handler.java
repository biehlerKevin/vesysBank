package rmi;

import bank.BankDriver2;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Handler extends UnicastRemoteObject implements BankRemote.RemoteUpdateHandler{

    private BankDriver2.UpdateHandler handler;

    public Handler(BankDriver2.UpdateHandler handler) throws RemoteException{
        this.handler = handler;
    }

    @Override
    public void accountChanged(String id) throws IOException {
        handler.accountChanged(id);
    }
}
