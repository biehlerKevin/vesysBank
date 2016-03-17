package http;

import bank.*;
import bank.Request.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

/**
 * Created by mfrey on 25/02/2016.
 */
public class WebDriver implements BankDriver {

	Socket s;
	Bank bank;
	URL url;
	HttpURLConnection urlCon;

	ObjectOutputStream oos;
	ObjectInputStream ois;

	@Override
	public void connect(String[] args) throws IOException {

		bank = new Bank();
		url = new URL("http://localhost:8080/bank");
		urlCon = (HttpURLConnection) url.openConnection();
		urlCon.setRequestMethod("POST");
		urlCon.setDoOutput(true); // to be able to write.
		urlCon.setDoInput(true); // to be able to read.
		urlCon.connect();
		System.out.println("Client connected");


		try{
		oos = new ObjectOutputStream(urlCon.getOutputStream());
		System.out.println("OutputStream Client");
		ois = new ObjectInputStream(urlCon.getInputStream());
		System.out.println("InputStream Client");
		} catch(IOException e){
			
		}


	}

	@Override
	public void disconnect() throws IOException {
		oos.writeObject(null);
		urlCon.disconnect();
		System.out.println("disconnected.");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();

		@Override
		public Set<String> getAccountNumbers() {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			Set<String> accountNumbers = null;
			try {
				oos.writeObject(new GetAccountNumbers());
				accountNumbers = (Set<String>) ois.readObject();
				for (String accountNumber : accountNumbers) {
					if (!accounts.containsKey(accountNumber)) {
						accounts.put(accountNumber, new Account(accountNumber));
					}
				}
			} catch (IOException | ClassNotFoundException ex) {

			}
			return accountNumbers;
		}

		@Override
		public String createAccount(String owner) {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			String accountNumber = null;
			try {
				oos.writeObject(new CreateAccount(owner));
				accountNumber = (String) ois.readObject();
				accounts.put(accountNumber, new Account(accountNumber));
			} catch (IOException e) {
				e.printStackTrace();
			}

			catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			return accountNumber;
		}

		@Override
		public boolean closeAccount(String number) {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			Boolean success = false;
			try {
				oos.writeObject(new CloseAccount(number));
				success = (Boolean) ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {

			}
			return success;
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			if (amount < 0)
				throw new IllegalArgumentException();

			Object response = null;
			try {
				oos.writeObject(new Transfer(from.getNumber(), to.getNumber(), amount));
				response = ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {

			}
			if (response != null) {
				if (response instanceof OverdrawException) {
					throw (OverdrawException) response;
				} else if (response instanceof InactiveException) {
					throw (InactiveException) response;
				}
			}
		}
	}

	class Account implements bank.Account {
		private String number;

		Account(String number) {
			this.number = number;
		}

		@Override
		public double getBalance() {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			Object response = null;
			try {
				oos.writeObject(new GetBalance(number));
				response = ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {

			}

			if (response instanceof Double) {
				return (Double) response;
			}
			return 0;
		}

		@Override
		public String getOwner() {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			Object response = null;
			try {
				oos.writeObject(new GetOwner(number));
				response = ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {

			}
			return (String) response;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			Object response = null;
			try {
				oos.writeObject(new IsActive(number));
				response = ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {

			}
			if (response instanceof Boolean) {
				return (Boolean) response;
			}
			return false;
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			if (amount < 0)
				return;

			Object response = null;
			try {
				oos.writeObject(new Deposit(number, amount));
				response = ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {

			}

			if (response instanceof InactiveException) {
				throw (InactiveException) response;
			}
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {
			try{
				oos = new ObjectOutputStream(urlCon.getOutputStream());
				System.out.println("OutputStream Client");
				ois = new ObjectInputStream(urlCon.getInputStream());
				System.out.println("InputStream Client");
				} catch(IOException e){
					
				}
			if (amount < 0)
				return;

			Object response = null;
			try {
				oos.writeObject(new Withdraw(number, amount));
				response = ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {

			}

			if (response instanceof InactiveException) {
				throw (InactiveException) response;
			} else if (response instanceof OverdrawException) {
				throw (OverdrawException) response;
			}
		}

	}
}
