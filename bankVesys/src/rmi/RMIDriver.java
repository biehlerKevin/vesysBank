package rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;

public class RMIDriver implements BankDriver {

	RMIBank bank;

	@Override
	public void connect(String[] args) {
		try {
			BankRMIService b = (BankRMIService) Naming.lookup("rmi://localhost/BankRMIService");

			this.bank = new RMIBank(b);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
	}

	@Override
	public Bank getBank() {
		return this.bank;
	}

	static class RMIBank implements bank.Bank {
		public BankRMIService service;

		public RMIBank(BankRMIService web) {
			this.service = web;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			Set<String> activeAccounts = new HashSet<String>(Arrays.asList(service.getAccountNumbers()));
			return activeAccounts;
		}

		@Override
		public String createAccount(String owner) throws IOException {
			return service.createAccount(owner);
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			return service.closeAccount(number);
		}

		@Override
		public bank.Account getAccount(String number) throws IOException {
			bank.Account res;
			if (service.getAccount(number).equals("")) {
				System.out.println("Account not existing");
				res = null;
			} else
				res = new RMIAccount(number, service);
			return res;
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException {
			service.transfer(from.getNumber(), to.getNumber(), amount);

		}

		static class RMIAccount implements bank.Account {

			private String number;
			BankRMIService service;

			RMIAccount(String number, BankRMIService svc) {
				this.number = number;
				this.service = svc;

			}

			@Override
			public double getBalance() throws IOException {
				return service.getBalance(this.number);
			}

			@Override
			public String getOwner() throws IOException {
				return service.getOwner(this.number);
			}

			@Override
			public String getNumber() {
				return this.number;
			}

			@Override
			public boolean isActive() throws IOException {
				return service.isActive(this.number);
			}

			@Override
			public void deposit(double amount) throws InactiveException, IOException {
				service.deposit(this.number, amount);
			}

			@Override
			public void withdraw(double amount) throws InactiveException, OverdrawException, IOException {
				service.withdraw(this.number, amount);
			}

		}

	}
}
