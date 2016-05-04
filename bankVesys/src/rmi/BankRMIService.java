package rmi;

import java.io.IOException;

import bank.InactiveException;
import bank.OverdrawException;

public interface BankRMIService extends java.rmi.Remote {

	String createAccount(String owner)throws IOException;

	boolean closeAccount(String number)throws IOException;

	void deposit(String number, double amount) throws IOException, InactiveException;

	void withdraw(String number, double amount) throws IOException, InactiveException, OverdrawException;

	void transfer(String from, String to, double amount) throws IOException, InactiveException, OverdrawException;

	String getOwner(String number) throws IOException;

	double getBalance(String number) throws IOException;

	boolean isActive(String number) throws IOException;

	String[] getAccountNumbers() throws IOException;

	String getAccount(String number)throws IOException;

}
