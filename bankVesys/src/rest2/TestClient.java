package rest2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class TestClient {

    public static void main(String[] args) {
        try {

            Client client = ClientBuilder.newClient();

            WebTarget webTarget = client.target("http://localhost:8080/base").path("accounts");

            Invocation.Builder invocationBuilder = webTarget.request();

            Response response = invocationBuilder.get();
            System.out.println(response.getStatus());
            System.out.println(response.readEntity(String.class));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}