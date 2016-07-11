package rmi;

import bank.Bank;
import bank.BankDriver2;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class Driver implements BankDriver2 {

    private BankRemote bank;

    @Override
    public void connect(String[] args) throws IOException {
        String host = args[0];
        try{
            bank = (BankRemote) Naming.lookup("rmi://"+host+"/Bank");
        }catch (NotBoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() throws IOException {
        bank = null;
    }

    @Override
    public Bank getBank() {
        return bank;
    }

    @Override
    public void registerUpdateHandler(UpdateHandler handler) throws IOException{
        bank.registerUpdateHandler(new Handler(handler));
    }
}
