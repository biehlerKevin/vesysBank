package rmi;

import java.rmi.Naming;

public class TestClient {

	public static void main(String[] args) throws Exception {
		BankRMIService b = (BankRMIService) Naming.lookup("rmi://localhost/BankRMIService");

		System.out.println(b.createAccount("kevin"));
		b.deposit("0", 100);
		System.out.println(b.getBalance("0"));
	}

}
