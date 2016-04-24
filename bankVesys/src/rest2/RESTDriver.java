package rest2;

import bank.InactiveException;
import bank.OverdrawException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RESTDriver implements bank.BankDriver {
	private Bank bank = new Bank();
	WebTarget webTarget;

	@Override
	public void connect(String[] args) throws IOException {
		Client client = ClientBuilder.newClient();
		webTarget = client.target(args[0]).path("accounts");
	}

	@Override
	public void disconnect() throws IOException {

	}

    /*
    used when accessing account data like balance, owner and active
     */
	private JSONObject getJSONObject(WebTarget webTarget){
		Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String jsonString = response.readEntity(String.class);
		JSONParser jsonParser = new JSONParser();
		try {
			return (JSONObject)jsonParser.parse(jsonString);
		} catch (ParseException e) {
			System.err.println(jsonString + "is not a valid JSON String!");
		}
		return new JSONObject();
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	class Bank implements bank.Bank {

		@Override
		public Set<String> getAccountNumbers() {
			Set<String> set = new HashSet<>();
			JSONObject obj = getJSONObject(webTarget);
            if(obj != null){
                JSONArray jsonArray = (JSONArray) obj.get("accounts");
                jsonArray.forEach(a->set.add((String)a));
            }
			return set;
		}

		@Override
		public String createAccount(String owner) {
			Invocation.Builder invocationBuilder = webTarget.request();
			Response response = invocationBuilder.post(Entity.text(owner));
			return response.readEntity(String.class);
		}

		@Override
		public boolean closeAccount(String number) {
            if(number.isEmpty()) return false;

			Invocation.Builder invocationBuilder = webTarget.path(number).request();
			Response response = invocationBuilder.delete();
			return response.getStatus() == 200;
		}

		@Override
		public bank.Account getAccount(String number) {
			// an empty string would call /accounts and not /accounts/{id}
			if(number.isEmpty()) return null;

			JSONObject obj = getJSONObject(webTarget.path(number));
			// if empty then account does not exist
			if(obj.isEmpty()) return null;

			String owner = (String) obj.get("owner");
			double balance = (double) obj.get("balance");
			return new Account(owner, number, balance);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {

			if(amount < 0) throw new IllegalArgumentException();

			Invocation.Builder invocationBuilder = webTarget.path(from.getNumber()).path("transfer").path(to.getNumber()).request();
			Response response = invocationBuilder.post(Entity.text(amount));
			int code = response.getStatus();
			switch (code){
				case 401:
					throw new OverdrawException();
				case 410:
					throw new InactiveException();
				default:
					break;
			}
		}

	}

	class Account implements bank.Account {
		private String number;
		private String owner;

		Account(String owner, String number, double balance) {
			this.owner = owner;
			this.number = number;
		}

		@Override
		public double getBalance() {
			JSONObject obj = getJSONObject(webTarget.path(number));
			return (double)obj.get("balance");
		}

		@Override
		public String getOwner() {
			return owner;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			JSONObject obj = getJSONObject(webTarget.path(number));
			return (boolean)obj.get("active");
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			if(amount < 0) return;

			Invocation.Builder invocationBuilder = webTarget.path(number).path("deposit").request();
			Response response = invocationBuilder.post(Entity.text(String.valueOf(amount)));

			if(response.getStatus() == 410) throw new InactiveException();
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {
			if(amount < 0) return;

			Invocation.Builder invocationBuilder = webTarget.path(number).path("withdraw").request();
			Response response = invocationBuilder.post(Entity.text(String.valueOf(amount)));

			switch (response.getStatus()){
				case 401:
					throw new OverdrawException();
				case 410:
					throw new InactiveException();
				default:
					break;
			}

		}

	}

}