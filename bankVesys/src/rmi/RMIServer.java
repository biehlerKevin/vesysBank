package rmi;

import java.rmi.Naming;

public class RMIServer {


	public static void main(String args[]) throws Exception {
	BankRMIService b = new BankRMIServiceImpl();
	Naming.bind("BankRMIService", b);
	}
	
	
}
