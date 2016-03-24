package soap;

import javax.xml.ws.Endpoint;

public class BankWebservicePublisher {
	
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9876/bank/jaxws", new BankWebserviceImpl(new MyBank()));
		System.out.println("Service published");
	}

}
