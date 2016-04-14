package rest;

import java.io.IOException;
import java.net.URI;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.jersey.api.core.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;




public class RestServer {

	
	public static void main(String[] args) throws IOException{
		String uri = "http://localhost:5555";
		ResourceConfig rc = new ApplicationAdapter(new BankApplication());
		
		System.out.println("Starting grizzly...");
		
		HttpServer httpServer =	GrizzlyServerFactory.createHttpServer(uri, rc);
		
		System.in.read();
		httpServer.shutdown();
	}
}
