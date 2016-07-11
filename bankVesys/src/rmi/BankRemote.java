package rmi;

import bank.Bank;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by mfrey on 10/07/2016.
 */
public interface BankRemote extends Bank,Remote {

    interface RemoteUpdateHandler extends Remote{
        void accountChanged(String id) throws IOException;
    }

    public void registerUpdateHandler(RemoteUpdateHandler handler) throws RemoteException;
}
