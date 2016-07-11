package rmi;

import bank.Bank;
import bank.local.Driver;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String args[]) throws IOException {

        try{
            LocateRegistry.createRegistry(1099);
            BankRemote bank = new BankImpl(new bank.local.Driver.Bank());
            Naming.rebind("rmi://localhost:1099/Bank", bank);
            System.out.println("Bank Server started");
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
