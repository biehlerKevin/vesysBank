package soap;

import java.io.IOException;

import javax.jws.WebService;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;


@WebService(endpointInterface = "soap.BankWebservice")
public class BankWebserviceImpl implements BankWebservice{
	
	private MyBank bank;
	
	public BankWebserviceImpl(MyBank bank){
		this.bank = bank;
	}

	@Override
	public String createAccount(String owner) {
		return this.bank.createAccount(owner);
	}

	@Override
	public boolean closeAccount(String number){
		return this.bank.closeAccount(number);
	}

	@Override
	public void deposit(String number, double amount)
			throws MyIllegalArgumentException, IOException, InactiveException {
		try {
			this.bank.getAccount(number).deposit(amount);	
		} catch (IllegalArgumentException e) {
			throw new MyIllegalArgumentException();
		}
		
	}

	@Override
	public void withdraw(String number, double amount)
			throws MyIllegalArgumentException, IOException, InactiveException, OverdrawException {
		try {
			this.bank.getAccount(number).withdraw(amount);
		} catch (IllegalArgumentException e) {
			throw new MyIllegalArgumentException();
		}
		
	}

	@Override
	public void transfer(String from, String to, double amount)
			throws MyIllegalArgumentException, IOException, InactiveException, OverdrawException {
		try {
			bank.transfer(bank.getAccount(from), bank.getAccount(to), amount);
		} catch (IllegalArgumentException e) {
			throw new MyIllegalArgumentException();
		}
		
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
	public double getBalance(String number) throws IOException, MyIllegalArgumentException {
		if (this.bank.getAccount(number) == null) 
			throw new MyIllegalArgumentException();
		
		return this.bank.getAccount(number).getBalance();
	}

	@Override
	public boolean isActive(String number) throws IOException, MyIllegalArgumentException {
		if (this.bank.getAccount(number) == null) 
			throw new MyIllegalArgumentException();
		
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
