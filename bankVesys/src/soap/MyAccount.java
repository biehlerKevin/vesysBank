package soap;

import bank.InactiveException;
import bank.OverdrawException;

public class MyAccount implements bank.Account{
	
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		MyAccount(String owner, String number) {
			this.owner = owner;
			this.number = number;
			this.balance = 0;
		}

		@Override
		public double getBalance() {
			return balance;
		}

		@Override
		public String getOwner() {
			return owner;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			if(!active) throw new InactiveException();
			if(amount < 0) return;

			balance += amount;
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {
			if(!active) throw new InactiveException();
			if(balance < amount) throw new OverdrawException();
			if(amount < 0) return;

			balance -= amount;
		}
		
		public void setActive(boolean active){
			this.active = active;
		}

	}

