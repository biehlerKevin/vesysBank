package websockets;

import bank.Bank;
import bank.BankDriver2;
import bank.Client;
import jmr.ClientBank;
import jmr.Request;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by mfrey on 11/07/2016.
 */
@ClientEndpoint(decoders = RequestDecoder.class, encoders = RequestEncoder.class)
public class Driver implements BankDriver2 {
    private Bank bank;
    private CompletableFuture<Request> future;
    private Session session;

    private List<UpdateHandler> listeners = new CopyOnWriteArrayList<>();

    @OnMessage
    public void onMessage(Request request) {
        future.complete(request);
    }

    @OnMessage
    public void onMessage(String id) {
        for (UpdateHandler h : listeners) {
            try {
                h.accountChanged(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerUpdateHandler(UpdateHandler handler) throws IOException {
        listeners.add(handler);
    }

    @Override
    public void connect(String[] args) throws IOException {
        try {
            URI uri = new URI("ws://" + args[0] + "/ws/bank");
            System.out.println("connecting to " + uri);
            ClientManager client = ClientManager.createClient();
            session = client.connectToServer(this, uri);
        } catch (URISyntaxException | DeploymentException e) {
            e.printStackTrace();
        }

        bank = new ClientBank(req -> {
            try {
                future = new CompletableFuture<>();
                session.getBasicRemote().sendObject(req);
                return future.get();
            } catch (EncodeException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        );
    }

    @Override
    public void disconnect() throws IOException {
        bank = null;
        session.close();
        System.out.println("disconnected...");
    }

    @Override
    public Bank getBank() {
        return bank;
    }
}
