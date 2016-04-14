package rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class BankApplication extends Application{

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public BankApplication(){
		classes.add(BankResources.class);
		singletons.add(new BankResources());
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons(){
		return singletons;
	}

}
