package rmi;

import java.io.IOException;

import bank.InactiveException;
import bank.OverdrawException;
import soap.MyBank;

public class BankRMIServiceImpl extends java.rmi.server.UnicastRemoteObject implements BankRMIService {

	private MyBank bank;

	public BankRMIServiceImpl(MyBank bank) throws IOException {
		this.bank = bank;
	}

	@Override
	public String createAccount(String owner) {
		return this.bank.createAccount(owner);
	}

	@Override
	public boolean closeAccount(String number) {
		return this.bank.closeAccount(number);
	}

	@Override
	public void deposit(String number, double amount) throws IOException, InactiveException {
		this.bank.getAccount(number).deposit(amount);

	}

	@Override
	public void withdraw(String number, double amount) throws IOException, InactiveException, OverdrawException {

		this.bank.getAccount(number).withdraw(amount);

	}

	@Override
	public void transfer(String from, String to, double amount)
			throws IOException, InactiveException, OverdrawException {
		bank.transfer(bank.getAccount(from), bank.getAccount(to), amount);

	}

	@Override
	public String getOwner(String number) throws IOException {
		bank.Account acc = this.bank.getAccount(number);
		if (acc != null)
			return acc.getOwner();
		else
			return "";
	}

	@Override
	public double getBalance(String number) throws IOException {

		return this.bank.getAccount(number).getBalance();
	}

	@Override
	public boolean isActive(String number) throws IOException {
		return this.bank.getAccount(number).isActive();
	}

	@Override
	public String[] getAccountNumbers() throws IOException {
		return this.bank.getAccountNumbers().toArray(new String[0]);
	}

	@Override
	public String getAccount(String number) {
		if (bank.getAccount(number) != null)
			return number;
		else {
			System.out.println("Account no existing");
			return "";
		}
	}
}
