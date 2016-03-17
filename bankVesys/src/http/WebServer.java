package http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.Request.CloseAccount;
import bank.Request.CreateAccount;
import bank.Request.Deposit;
import bank.Request.GetAccount;
import bank.Request.GetAccountNumbers;
import bank.Request.GetBalance;
import bank.Request.GetOwner;
import bank.Request.IsActive;
import bank.Request.Request;
import bank.Request.Transfer;
import bank.Request.Withdraw;
import bank.local.Driver;

public class WebServer {

	public static void main(String args[]) throws IOException {

        Driver driver = new Driver();
        driver.connect(null);
        Bank bank = driver.getBank();
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/bank", new MyHandler(bank));
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server listening on port 8080");

	}

	static class MyHandler implements HttpHandler {

		Bank bank;
		ObjectOutputStream oos;
        ObjectInputStream ois;
		
		public MyHandler(Bank b){
			this.bank = b;
		}
		
		
		@Override
		public void handle(HttpExchange exchange) throws IOException {

		    String respHeader = "Hello bank!\n";
		    exchange.sendResponseHeaders(200, respHeader.length());
			
            ois = new ObjectInputStream(exchange.getRequestBody());
			System.out.println("Opened Handler inputstream");
			oos = new ObjectOutputStream(exchange.getResponseBody());
			System.out.println("Opened Handler outputstream");


            Object request;
            Object response = null;
            try {
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
			} catch (ClassNotFoundException | IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("done serving ");
			
		}
	}
}
