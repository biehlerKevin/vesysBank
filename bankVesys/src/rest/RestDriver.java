package rest;

import java.io.IOException;
import java.util.Set;

import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;


public class RestDriver implements BankDriver {

	private RestBank bank;
	
	
	@Override
	public void connect(String[] args) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Bank getBank() {
		return this.bank;
	}

	static class RestBank implements bank.Bank {

		public RestBank() {
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			return null;
		}

		@Override
		public String createAccount(String owner) throws IOException {
			return null;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			return false;
		}

		@Override
		public bank.Account getAccount(String number) throws IOException {
			return null;
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {

		}

		static class RestAccount implements bank.Account {

			private String number;

			RestAccount(String number) {
				this.number = number;

			}

			@Override
			public double getBalance() throws IOException {
				return 0;
			}

			@Override
			public String getOwner() throws IOException {
				return null;
			}

			@Override
			public String getNumber() {
				return this.number;
			}

			@Override
			public boolean isActive() throws IOException {
				return false;
			}

			@Override
			public void deposit(double amount) throws InactiveException, IOException, IllegalArgumentException {

			}

			@Override
			public void withdraw(double amount)
					throws InactiveException, OverdrawException, IOException, IllegalArgumentException {

			}

		}
	}
}
