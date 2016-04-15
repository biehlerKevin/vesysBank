package rest2;

import bank.Account;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("accounts")
public class AccountsResource {
    @Inject
    BankDriver driver;

    @GET
    @Produces("application/json")
    public String getAccountNumbers() throws IOException {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        driver.getBank().getAccountNumbers().forEach(a->array.add(a));
        obj.put("accounts",array);
        return obj.toJSONString();
    }

    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public String createAccount(String owner, @Context UriInfo uriInfo) throws IOException {
        String num = driver.getBank().createAccount(owner);
        //return uriInfo.getAbsolutePath().toString() + "/" + num;
        return num;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getAccount(@PathParam("id") String id) throws IOException {
        Account account = driver.getBank().getAccount(id);
        if(account == null) return Response.status(404).build();

        JSONObject obj = new JSONObject();
        obj.put("owner", account.getOwner());
        obj.put("balance", account.getBalance());
        obj.put("active", account.isActive());
        return Response.ok(obj.toJSONString()).build();
    }

    @PUT
    @Path("{id}")
    @Produces("text/plain")
    public Response setAccountBalance(@PathParam("id") String id, String amount) throws IOException {
        Account account = driver.getBank().getAccount(id);
        if(account == null) return Response.status(404).build();

        try {
            double val = Double.parseDouble(amount);
            if(val < 0){
                val = Math.abs(val);
                account.withdraw(val);
            }else{
                account.deposit(val);
            }
            return Response.ok().build();

        } catch (OverdrawException e) {
            return Response.status(401).build();
        } catch (InactiveException e) {
            return Response.status(410).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces("text/plain")
    public Response deleteAccount(@PathParam("id") String id) throws IOException {
        Account account = driver.getBank().getAccount(id);
        if(account == null) return Response.status(404).build();

        if(driver.getBank().closeAccount(id)){
            return Response.ok().build();
        }else{
            return Response.status(401).build();
        }

    }

    @HEAD
    @Path("{id}")
    @Produces("text/plain")
    public Response isAccountActive(@PathParam("id") String id) throws IOException {
        Account account = driver.getBank().getAccount(id);
        if(account == null) return Response.status(404).build();

        if(driver.getBank().getAccount(id).isActive()){
            return Response.ok().build();
        }else{
            return Response.status(410).build();
        }
    }

    @POST
    @Path("{from}/{to}")
    @Produces("text/plain")
    public Response transfer(@PathParam("from") String from, @PathParam("to") String to, String amount) throws IOException {
        Account fromAccount = driver.getBank().getAccount(from);
        Account toAccount = driver.getBank().getAccount(to);
        if(fromAccount == null || toAccount == null) return Response.status(404).build();
        if(!(fromAccount.isActive() && toAccount.isActive()));

        try {
            double val = Double.parseDouble(amount);
            driver.getBank().transfer(fromAccount, toAccount, val);
            return Response.ok().build();
        } catch (OverdrawException e) {
            return Response.status(401).build();
        } catch (InactiveException e) {
            return Response.status(410).build();
        }
    }
}
