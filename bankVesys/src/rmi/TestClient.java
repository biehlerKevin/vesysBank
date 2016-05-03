package rmi;

import java.rmi.Naming;

public class TestClient {
	
	public static void main(String[] args) throws Exception {
		BankRMIService b = (BankRMIService)Naming.lookup(
		"rmi://localhost/BankRMIService");
		
		
	}

}
