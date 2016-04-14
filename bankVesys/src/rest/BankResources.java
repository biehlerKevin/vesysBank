package rest;

import javax.ws.rs.Path;

import com.sun.jersey.spi.resource.Singleton;

import soap.MyBank;

@Singleton
@Path("/bank")
public class BankResources {

	private MyBank bank;
	
	
	public BankResources(){
		bank = new MyBank();
	}
	
	//GET
	
	//POST
	
	//DELETE
}
