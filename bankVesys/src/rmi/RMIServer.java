package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


import soap.MyBank;

public class RMIServer {

	public static void main(String args[]) throws Exception {
		
		try {
			LocateRegistry.createRegistry(1099);
			}
			catch (RemoteException e) {
			System.out.println(">> registry could not be exported");
			System.out.println(">> probably another registry already runs on 1099");
			}
		BankRMIService b = new BankRMIServiceImpl(new MyBank());
		Naming.bind("BankRMIService", b);
		
		System.out.println("BankRMI started");
	}

}
