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

	}

	@Override
	public void disconnect() throws IOException {
		oos.writeObject(null);
		urlCon.disconnect();
		System.out.println("disconnected.");
	}

	public Object makeRequest(Request request){
		Object response = null;
		try{
			urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setDoOutput(true); // to be able to write.
			urlCon.setDoInput(true); // to be able to read.
			urlCon.connect();

			oos = new ObjectOutputStream(urlCon.getOutputStream());
			oos.writeObject(request);
			oos.close();

			ois = new ObjectInputStream(urlCon.getInputStream());
			response = ois.readObject();
			ois.close();

		} catch(ClassNotFoundException | IOException e){
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();

		@Override
		public Set<String> getAccountNumbers() {
			Set<String> response = (Set<String>)makeRequest(new GetAccountNumbers());
			response.forEach(e->{
				if(!accounts.containsKey(e)){
					accounts.put(e, new Account(e));
				}
			});
			return response;
		}

		@Override
		public String createAccount(String owner) {
			String accountNumber = (String)makeRequest(new CreateAccount(owner));
			accounts.put(accountNumber, new Account(accountNumber, owner));

			return accountNumber;
		}

		@Override
		public boolean closeAccount(String number) {
			return (Boolean) makeRequest(new CloseAccount(number));
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {
			Object response = makeRequest(new Transfer(from.getNumber(), to.getNumber(), amount));

			if(amount < 0) throw new IllegalArgumentException();

			if (response != null) {
				if (response instanceof OverdrawException) {
					throw (OverdrawException) response;
				} else if (response instanceof InactiveException) {
					throw (InactiveException) response;
				}else if (response instanceof IllegalArgumentException) {
					throw (IllegalArgumentException) response;
				}
			}
		}
	}

	class Account implements bank.Account {
		private String number;
		private String owner;

		Account(String number) {
			this.number = number;
		}

		Account(String number, String owner) {
			this.number = number;
			this.owner = owner;
		}

		@Override
		public double getBalance() {
			Object response = makeRequest(new GetBalance(number));

			if (response instanceof Double) {
				return (Double) response;
			}
			return 0;
		}

		@Override
		public String getOwner() {
			if(owner == null){
				owner = (String)makeRequest(new GetOwner(number));
			}

			return owner;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			return (Boolean)makeRequest(new IsActive(number));
		}

		@Override
		public void deposit(double amount) throws InactiveException {

			Object response = makeRequest(new Deposit(number, amount));

			if (response instanceof InactiveException) {
				throw (InactiveException) response;
			}
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {

			Object response = makeRequest(new Withdraw(number, amount));

			if (response instanceof InactiveException) {
				throw (InactiveException) response;
			} else if (response instanceof OverdrawException) {
				throw (OverdrawException) response;
			}
		}

	}
}
