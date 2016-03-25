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
		
		System.out.println("connected...");

	}

}
