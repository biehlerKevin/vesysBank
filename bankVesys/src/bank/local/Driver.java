/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.local;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import bank.InactiveException;
import bank.OverdrawException;

public class Driver implements bank.BankDriver {
	private Bank bank = null;

	@Override
	public void connect(String[] args) {
		bank = new Bank();
		System.out.println("connected...");
	}

	@Override
	public void disconnect() {
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	static class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();
		private int numberOfAccounts = 0;

		@Override
		public Set<String> getAccountNumbers() {
			return accounts.values().stream().filter(Account::isActive).map(Account::getNumber).collect(Collectors.toSet());
		}

		@Override
		public String createAccount(String owner) {
			String accountNumber = Integer.toString(numberOfAccounts);
			numberOfAccounts++;
			Account account = new Account(owner, accountNumber);
			accounts.put(accountNumber, account);
			return accountNumber;
		}

		@Override
		public boolean closeAccount(String number) {
			Account acc = accounts.get(number);
			if(acc.getBalance() > 0 || !acc.isActive()) return false;

			acc.active = false;
			return true;
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {

			if(amount < 0) throw new IllegalArgumentException();
			if(amount > from.getBalance()) throw new OverdrawException();
			if(!(from.isActive() && to.isActive())) throw new InactiveException();
			from.withdraw(amount);
			to.deposit(amount);
		}

	}

	static class Account implements bank.Account {
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		Account(String owner, String number) {
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

	}

}