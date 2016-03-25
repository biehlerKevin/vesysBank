package soap;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class Client {
	
	
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost:9876/soap?wsdl");
		
		QName qname = new QName("http://soap/","BankWebserviceImplService");
		
		Service service = Service.create(url, qname);
		
		BankWebservice web = service.getPort(BankWebservice.class);
		
		System.out.println(web.createAccount("Kevin1"));
		System.out.println(web.createAccount("Kevin2"));
		web.deposit("0", 100);
		System.out.println(web.getBalance("0"));
		web.withdraw("0", 50);
		System.out.println(web.getBalance("0"));

		
		System.out.println("connected...");

	}

}
