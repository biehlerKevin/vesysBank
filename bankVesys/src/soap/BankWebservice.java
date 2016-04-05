package soap;

import java.io.IOException;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import bank.InactiveException;
import bank.OverdrawException;

@WebService

public interface BankWebservice {
	
	String createAccount(String owner);
	
	boolean closeAccount(String number);
	
	void deposit(String number, double amount) 
			throws MyIllegalArgumentException, IOException, InactiveException;
	
	void withdraw(String number, double amount) 
			throws MyIllegalArgumentException, IOException, InactiveException, OverdrawException;
	
	void transfer(String from, String to, double amount) 
			throws MyIllegalArgumentException, IOException, InactiveException, OverdrawException;
	
	String getOwner(String number) 
			throws IOException;
	
	double getBalance(String number) 
			throws IOException, MyIllegalArgumentException;
	
	boolean isActive(String number) 
			throws IOException, MyIllegalArgumentException;
	
	String[] getAccountNumbers() 
			throws IOException;
	
	String getAccount(String number);
	

}
