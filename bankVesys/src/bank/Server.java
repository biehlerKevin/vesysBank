package bank;

import bank.Request.*;
import bank.local.Driver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mfrey on 25/02/2016.
 */
public class Server {
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        Driver driver = new Driver();
        driver.connect(null);
        Bank bank = driver.getBank();

        try (ServerSocket server = new ServerSocket(6789)) {
            while (true) {
                Socket s = server.accept();
                Thread worker = new Worker(s, bank);
                worker.start();

            }
        }
    }

    static class Worker extends Thread{

        Socket s;
        Bank bank;

        public Worker(Socket s, Bank bank){
            this.s = s;
            this.bank = bank;
        }

        public void run(){
            try {
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());


                Object request;
                Object response = null;
                while ((request = ois.readObject()) != null) {
                    if(request instanceof Request){
                        System.out.println("=> " + request.getClass());

                        if(request instanceof CreateAccount){
                            String owner = ((CreateAccount) request).owner;
                            response = bank.createAccount(owner);
                        }else if(request instanceof CloseAccount){
                            String accountNumber = ((CloseAccount) request).accountNumber;
                            response = bank.closeAccount(accountNumber);
                        }else if(request instanceof GetAccountNumbers){
                            response = bank.getAccountNumbers();
                        }else if(request instanceof GetAccount){
                            String accountNumber = ((GetAccount) request).accountNumber;
                            response = bank.getAccount(accountNumber);
                        }else if(request instanceof Transfer){
                            Transfer transfer = ((Transfer) request);
                            Account from = bank.getAccount(transfer.fromAccountNumber);
                            Account to = bank.getAccount(transfer.toAccountNumber);
                            try {
                                bank.transfer(from, to, transfer.amount);
                                response = true;
                            } catch (InactiveException e) {
                                response = new InactiveException();
                            } catch (OverdrawException e) {
                                response = new OverdrawException();
                            }
                        }else if(request instanceof GetBalance){
                            String accountNumber = ((GetBalance) request).accountNumber;
                            response = bank.getAccount(accountNumber).getBalance();
                        }else if(request instanceof GetOwner){
                            String accountNumber = ((GetOwner) request).accountNumber;
                            response = bank.getAccount(accountNumber).getOwner();
                        }else if(request instanceof IsActive){
                            String accountNumber = ((IsActive) request).accountNumber;
                            response = bank.getAccount(accountNumber).isActive();
                        }else if(request instanceof Deposit){
                            Deposit deposit = ((Deposit) request);
                            try {
                                bank.getAccount(deposit.accountNumber).deposit(deposit.amount);
                                response = true;
                            } catch (InactiveException e) {
                                response = new InactiveException();
                            }
                        }else if(request instanceof Withdraw){
                            Withdraw withdraw = ((Withdraw) request);
                            try {
                                bank.getAccount(withdraw.accountNumber).withdraw(withdraw.amount);
                                response = true;
                            } catch (OverdrawException e) {
                                response = new OverdrawException();
                            } catch (InactiveException e) {
                                response = new InactiveException();
                            }
                        }
                        oos.writeObject(response);
                        if(response != null) System.out.println("<= " + response.toString());
                    }
                }
                System.out.println("done serving " + s);
                s.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
