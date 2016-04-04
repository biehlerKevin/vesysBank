package soap;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import bank.Account;
import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;

public class SoapDriver implements BankDriver{
	
	SoapBank bank;

	@Override
	public void connect(String[] args) throws IOException {
		URL url = new URL("http://localhost:9876/soap?wsdl");
		
		QName qname = new QName("http://soap/","BankWebserviceImplService");
		
		Service service = Service.create(url, qname);
		
		BankWebservice web = service.getPort(BankWebservice.class);
		
		this.bank = new SoapBank(web);
		
		System.out.println("connected...");
		
	}

	@Override
	public void disconnect() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Bank getBank() {
		return this.bank;
	}
	
	
	static class SoapBank implements bank.Bank{
		public BankWebservice service;
		
		public SoapBank(BankWebservice web){
			this.service = web;
		}

		@Override
		public String createAccount(String owner) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Account getAccount(String number) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void transfer(Account a, Account b, double amount)
				throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	static class SoapAccount implements bank.Account{

		@Override
		public String getNumber() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getOwner() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isActive() throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void withdraw(double amount)
				throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public double getBalance() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}
